package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.*;
import com.digitaldeparturesystem.pojo.Refreshtoken;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.response.ResponseStatus;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseResult insertStudent(Student student) {
        //第一步：检查当前用户名、姓名、部门是否已经注册
        String stuAccount = student.getStuNumber();
        if (TextUtils.isEmpty(stuAccount)) {
            return ResponseResult.FAILED("用户名不可以为空");
        }
        if (TextUtils.isEmpty(student.getStuName())){
            return ResponseResult.FAILED("姓名不可以为空");
        }
        Student stuFromDbByAccount = studentMapper.findOneByStudentAccount(stuAccount);
        if (stuFromDbByAccount != null) {
            return ResponseResult.FAILED("该用户已经注册");
        }
        if (TextUtils.isEmpty(student.getStuDept())){
            return ResponseResult.FAILED("该学生所属系不能为空");
        }
        if (TextUtils.isEmpty(student.getStuClass())){
            return ResponseResult.FAILED("该学生所属班级不能为空");
        }
        if (TextUtils.isEmpty(student.getStuContact())){
            return ResponseResult.FAILED("该学生联系方式不能为空");
        }
        if (TextUtils.isEmpty(student.getStuSex())){
            return ResponseResult.FAILED("该学生性别不能为空");
        }
        if (TextUtils.isEmpty(student.getStuInDate().toString())){
            return ResponseResult.FAILED("入学时间不能为空");
        }
        if (TextUtils.isEmpty(student.getStuOutDate().toString())){
            return ResponseResult.FAILED("毕业时间不能为空");
        }
        if (TextUtils.isEmpty(student.getStuAddress())){
            return ResponseResult.FAILED("该学生家庭地址不能为空");
        }
        if (TextUtils.isEmpty(student.getStuSpecialty())){
            return ResponseResult.FAILED("该学生专业不能为空");
        }
        if (TextUtils.isEmpty(student.getStuNation())){
            return ResponseResult.FAILED("该学生名族不能为空");
        }
        if (TextUtils.isEmpty(student.getStuFeature())){
            return ResponseResult.FAILED("该学生政治面貌不能为空");
        }
        if (TextUtils.isEmpty(student.getStuType())){
            return ResponseResult.FAILED("该学生类型不能为空");
        }
        if (TextUtils.isEmpty(student.getStuCredit())){
            return ResponseResult.FAILED("该学生学分不能为空");
        }
        if (TextUtils.isEmpty(student.getStuPhoneNumber())){
            return ResponseResult.FAILED("该学生电话号码不能为空");
        }
        //达到可以注册的条件
        //第六步：对密码进行加密
        String password = student.getStuPwd();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        student.setStuPwd(bCryptPasswordEncoder.encode(password));
        //第七布：补全数据
        student.setStuId(String.valueOf(idWorker.nextId()));
        student.setStuStatus("1");
        //添加权限
        Role roleFromDb = roleMapper.getRoleByCode("ROLE_STUDENT");
        Map<String,String> map = new HashMap<>();
        map.put("clerkId",student.getStuId());
        map.put("roleId",roleFromDb.getId());
        map.put("id",String.valueOf(idWorker.nextId()));
        userRoleMapper.addRoleToUser(map);
        //第八步：保存到数据库
        studentMapper.insertStudent(student);
        //第九步：返回结果
        return ResponseResult.GET(ResponseStatus.JOIN_SUCCESS);
    }

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private RefreshTokenMapper tokenMapper;

    @Override
    public Student checkStudent() {
        //拿到token_key(因为之前已经CookieUtils中setUpCookie时，把tokenKey通过(Constants.User.COOKIE_TOKEN_KEY,tokenKey)保存在cookie中了)
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.Clerk.COOKIE_TOKEN_KEY);
        log.info("checkClerk tokenKey ==> " + tokenKey);
        //解析
        Student studentFromToken = TokenUtils.parseStudentByTokenKey(redisUtils,tokenKey);
        if (studentFromToken == null){
            //说明解析出错了(过期了)
            //1、去mysql查询refreshToken
            Refreshtoken refreshToken = tokenMapper.findOneByTokenKey(tokenKey);
            //2、如果不存在，就是当前访问没有登录
            if (refreshToken == null) {
                log.info("refresh token is null =====");
                return null;
            }
            //3、如果存在，就解析refreshToken
            try {
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //5、如果refreshToken有效，创建新的token，和新的refreshToken
                //拿到用户id，去数据库查询，再在redis里面生成新的token
                String studentId = refreshToken.getUserId();
                Student studentFromDb = studentMapper.findOneByStudentAccount(studentId);
                //如果这样写，因为事务还没有提交，所以该用户的密码直接没了
                //userFromDb.setPassword("");
                //在redis里面创建新的token,因为到这里的时候，redis里面久的token已经过期自动删除了，所以这里不必再手动删除了
                String newTokenKey = createToken(getResponse(), studentFromDb);
                //返回token
                log.info("create new token and refresh token =====");
                return TokenUtils.parseStudentByTokenKey(redisUtils,newTokenKey);
            } catch (Exception exception) {
                log.info("refresh token 已经过期 =====");
                //4、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                return null;
            }
        }
        return studentFromToken;
    }

    public String createToken(HttpServletResponse response, Student student) {
        //删掉refreshToken的记录
        int deleteResult = tokenMapper.deleteAllByUserId(student.getStuId());
        log.info("deleteResult of refresh token ===> " + deleteResult);
        //生成token
        Map<String, Object> claims = ClaimsUtils.student2Claims(student);
        //token默认有效2个小时
        String token = JwtUtil.createToken(claims);
        //返回token的md5值，token会保存到redis里
        //前端访问的时候，携带token的md5key，从redis中获取即可（tokenKey作为key，是用来查找token的）
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2小时,key是tokenKey
        redisUtils.set(Constants.Clerk.KEY_TOKEN + tokenKey, token, Constants.TimeValueInSecond.HOUR_2);
        //把token写到cookies里，tokenKey在cookies里面的存活期我们不用关心，因为他只是一个key,只要redis里面的token过期了
        //那么这个key就没用了
        CookieUtils.setUpCookie(response, Constants.Clerk.COOKIE_TOKEN_KEY, tokenKey);
        //生成refreshToken,一个月的存活期
        String refreshTokenValue = JwtUtil.createRefreshToken(student.getStuId(), Constants.TimeValueInMillions.MONTH);
        //保存到数据库里面
        //refreshToken，tokenKey,用户ID，创建时间，更新时间
        Refreshtoken refreshToken = new Refreshtoken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(student.getStuId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        //保存进数据库
        tokenMapper.insertRefreshToken(refreshToken);
        return tokenKey;
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Resource
    private StudentMapper studentMapper;


    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Student student = studentMapper.findOneByStudentAccount(account);
        if (student == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return student;
    }
}
