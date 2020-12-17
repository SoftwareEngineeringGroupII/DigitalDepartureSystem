package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.RefreshTokenMapper;
import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.PasswordBean;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICommonService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
@Transactional
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private RedisUtils redisUtils;

    public static final int[] captcha_font_types = new int[]{
            Captcha.FONT_1,
            Captcha.FONT_2,
            Captcha.FONT_3,
            Captcha.FONT_4,
            Captcha.FONT_5,
            Captcha.FONT_6,
            Captcha.FONT_7,
            Captcha.FONT_8,
            Captcha.FONT_9,
            Captcha.FONT_10,
    };

    @Autowired
    private Random random;

    @Override
    public ResponseResult getMenu() {
        HttpServletRequest request = getRequest();
        //用用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        //权限信息
        Set<Authorities> authorityFromDB;
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            authorityFromDB = (Set<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + student.getStuId());
        }else{
            authorityFromDB = (Set<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + clerkFromToken.getClerkID());
        }
        //权限
        Set<Authorities> authorityList = new TreeSet<>(new Comparator<Authorities>() {
            @Override
            public int compare(Authorities o1, Authorities o2) {
                return (int)(o1.getIndex() - o2.getIndex());
            }
        });
        for (Authorities authorities : authorityFromDB) {
            authorityList.add(authorities);
        }
        return ResponseResult.SUCCESS("获取菜单成功").setData(authorityList);
    }

    @Override
    public ResponseResult getUserInfo() {
        //获取用户token
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            return ResponseResult.SUCCESS("查询用户信息成功").setData(student);
        }else{
            return ResponseResult.SUCCESS("查询用户信息成功").setData(clerkFromToken);
        }
    }

    @Override
    public void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception {
        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
            return;
        }
        long key = 01;
        try {
            key = Long.parseLong(captchaKey);
            //处理
        } catch (NumberFormatException e) {
            return;
        }
        //可以用
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        int captchaType = random.nextInt(3);
        Captcha targetCaptcha;
        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            targetCaptcha = new SpecCaptcha(200, 60, 5);
        } else if (captchaType == 1) {
            //gif类型
            targetCaptcha = new GifCaptcha(200, 60);
        } else {
            //算数类型
            targetCaptcha = new ArithmeticCaptcha(200, 60);
            targetCaptcha.setLen(2);//几位数运算，默认两位
            //((ArithmeticCaptcha)targetCaptcha).getArithmeticString();//获取运算的公式：3+2=？
        }
        int index = random.nextInt(captcha_font_types.length);
        log.info("captcha font type index ==> " + index);
        targetCaptcha.setFont(captcha_font_types[index]);
        String content = targetCaptcha.text().toLowerCase();//获取运算的结果：
        log.info("content ==> " + content);
        //保存到redis里面
        //删除的时机
        //1、自然过期，10分钟后自己删除
        //2、验证码用完以后删除
        //3、用完的情况：看get的地方
        redisUtils.set(Constants.Clerk.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);
        // 输出图片流
        targetCaptcha.out(response.getOutputStream());
    }

    @Autowired
    private TaskService taskService;

    /**
     * 发送邮箱验证码
     * 注册(register)：如果已经注册过了，就提示该邮箱已经注册
     * 找回密码(forget)：如果没有注册过，提示该邮箱没有注册
     * 修改邮箱（update）：如果已经注册了，提示该邮箱已经注册
     *
     * @param request
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendEmail(HttpServletRequest request, String emailAddress) {
        if (emailAddress == null) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }
//        //根据类型：查询邮箱是否存在
//        if ("register".equals(type) || "update".equals(type)) {
//            Clerk clerkByEmail = sectorMapper.findOneByEmail(emailAddress);
//            Student student = studentMapper.findOneByEmail(emailAddress);
//            if (clerkByEmail != null || student != null) {
//                return ResponseResult.FAILED("该邮箱已注册");
//            }
//        }
        //1、防止暴力发送(不断的发送):同一个邮箱，间隔要超过30s发一次，同一个Ip，最多只能发10次(如果是短信，最多只能发5次)
        String remoteAddr = request.getRemoteAddr();
        log.info("remoteAddr ==> " + remoteAddr);
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replaceAll(":", "\\_");
        }
        //拿出来，如果没有，就通过
        log.info("Constants.User.KEY_EMAIL_SEND_IP + remoteAddr ======> " + Constants.Clerk.KEY_EMAIL_SEND_IP + remoteAddr);
        Integer ipSendTime = (Integer) redisUtils.get(Constants.Clerk.KEY_EMAIL_SEND_IP + remoteAddr);
        if (ipSendTime != null && ipSendTime > 10) {
            return ResponseResult.FAILED("请发送的验证码请求太频繁");
        }
        Object hasEmailSend = redisUtils.get(Constants.Clerk.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (hasEmailSend != null) {
            return ResponseResult.FAILED("请发送的验证码请求太频繁");
        }
        //2、检查邮箱地址是否正确
        boolean isEmailFormat = TextUtils.isEmailAddressOk(emailAddress);
        if (!isEmailFormat) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += code;
        }
        log.info("send emial code ==> " + code);
        //3、发送验证码,6位数：100000~999999
        try {
            taskService.sendEmailVerifyCode(String.valueOf(code), emailAddress);
        } catch (Exception e) {
            return ResponseResult.FAILED("验证码发送失败，请稍后重试");
        }
        //4、做记录
        //发送记录：code
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        ipSendTime++;
        //1小时有效期,因为前面做了次数限定，所以不怕value在redis里面占据大量空间
        redisUtils.set(Constants.Clerk.KEY_EMAIL_SEND_IP + remoteAddr, ipSendTime, 60 * 60);
        //邮箱地址没有太大的意义，30s
        redisUtils.set(Constants.Clerk.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
        //保存code,10分钟有效
        redisUtils.set(Constants.Clerk.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
        return ResponseResult.SUCCESS("验证码发送成功");
    }

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private SectorMapper sectorMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseResult recoveredUserPassword(String verifyCode, String password,String userAccount) {
        if (password == null || TextUtils.isEmpty(password)){
            return ResponseResult.FAILED("密码不能为空");
        }
        //加密后得密码
        String changePwd = bCryptPasswordEncoder.encode(password);
        //判别用户
        Clerk clerkFromDb = sectorMapper.findOneByClerkAccount(userAccount);
        Student stuFromDb = studentMapper.findOneByStudentAccount(userAccount);
        Map<String,String> map = new HashMap<>();
        if (stuFromDb != null){
            if (!checkVerifyCode(verifyCode,stuFromDb.getStuContact())) {
                return ResponseResult.FAILED("验证码错误");
            }
            map.put("stuPwd",changePwd);
            map.put("stuID",stuFromDb.getStuId());
            studentMapper.updatePassword(map);
        }else if (clerkFromDb != null){
            if (!checkVerifyCode(verifyCode,clerkFromDb.getClerkEmail())) {
                return ResponseResult.FAILED("验证码错误");
            }
            map.put("clerkPwd",changePwd);
            map.put("clerkID",stuFromDb.getStuId());
            sectorMapper.updatePassword(map);
        } else{
            return ResponseResult.FAILED("用户不存在");
        }
        //修改密码
        return ResponseResult.SUCCESS("找回密码成功");
    }

    @Override
    public ResponseResult updateUserPassword(String captcha, String captchaKey, PasswordBean passwordBean) {
        String captchaValue = (String) redisUtils.get(Constants.Clerk.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captcha.equals(captchaValue)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }
        //当前用户
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        Student student;
        Map<String,String> map = new HashMap<>();
        //旧密码
        String oldPwd = passwordBean.getOldPwd();
        //新密码
        String newPwd = passwordBean.getNewPwd();
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            //检测旧密码
            Student stuFromDB = studentMapper.getStudentById(student.getStuId());
            if (!bCryptPasswordEncoder.matches(oldPwd,stuFromDB.getStuPwd())) {
                //不一样
                return ResponseResult.FAILED("旧密码错误");
            }
            map.put("stuPwd",bCryptPasswordEncoder.encode(newPwd));
            map.put("stuID",student.getStuId());
            studentMapper.updatePassword(map);
        }else{
            Clerk clerkFromDB = sectorMapper.findOneById(clerkFromToken.getClerkID());
            if (!bCryptPasswordEncoder.matches(oldPwd,clerkFromDB.getClerkPwd())) {
                //不一样
                return ResponseResult.FAILED("旧密码错误");
            }
            map.put("clerkPwd",bCryptPasswordEncoder.encode(newPwd));
            map.put("clerkID",clerkFromToken.getClerkID());
            sectorMapper.updatePassword(map);
        }
        return ResponseResult.SUCCESS("修改密码成功");
    }

    public ResponseResult updateUserPassword(PasswordBean passwordBean) {
        //当前用户
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        Student student;
        Map<String,String> map = new HashMap<>();
        //旧密码
        String oldPwd = passwordBean.getOldPwd();
        //新密码
        String newPwd = passwordBean.getNewPwd();
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            //检测旧密码
            Student stuFromDB = studentMapper.getStudentById(student.getStuId());
            if (!bCryptPasswordEncoder.matches(oldPwd,stuFromDB.getStuPwd())) {
                //不一样
                return ResponseResult.FAILED("旧密码错误");
            }
            map.put("stuPwd",bCryptPasswordEncoder.encode(newPwd));
            map.put("stuID",student.getStuId());
            studentMapper.updatePassword(map);
        }else{
            Clerk clerkFromDB = sectorMapper.findOneById(clerkFromToken.getClerkID());
            if (!bCryptPasswordEncoder.matches(oldPwd,clerkFromDB.getClerkPwd())) {
                //不一样
                return ResponseResult.FAILED("旧密码错误");
            }
            map.put("clerkPwd",bCryptPasswordEncoder.encode(newPwd));
            map.put("clerkID",clerkFromToken.getClerkID());
            sectorMapper.updatePassword(map);
        }
        return ResponseResult.SUCCESS("修改密码成功");
    }

    private boolean checkVerifyCode(String verifyCode,String email) {
        //根据邮箱去redis里拿验证x'c
        String redisVerifyCode = (String) redisUtils.get(Constants.Clerk.KEY_EMAIL_CODE_CONTENT + email);
        if (redisVerifyCode == null || !redisVerifyCode.equals(verifyCode)) {
            return false;
        }
        //进行对比
        redisUtils.del(Constants.Clerk.KEY_EMAIL_CODE_CONTENT + email);
        return true;
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }
}
