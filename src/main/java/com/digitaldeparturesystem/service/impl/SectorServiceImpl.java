package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.*;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.response.ResponseStatus;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.*;
import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
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
import java.util.*;

@Slf4j
@Service  //业务层
@Transactional
public class SectorServiceImpl implements ISectorService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    private Clerk clerkFromToken;

    @Resource
    private SectorMapper sectorMapper;

    @Resource
    private AdminMapper adminMapper;

    @Override
    public ResponseResult register(Clerk clerk, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request) {
        //第一步：检查当前用户名、姓名、部门是否已经注册
        String clerkAccount = clerk.getClerkAccount();
        if (TextUtils.isEmpty(clerkAccount)) {
            return ResponseResult.FAILED("用户名不可以为空");
        }
        if (TextUtils.isEmpty(clerk.getClerkName())){
            return ResponseResult.FAILED("姓名不可以为空");
        }
        if (TextUtils.isEmpty(clerk.getDepartment())){
            return ResponseResult.FAILED("部门不可以为空");
        }
        //数据库配置
        Clerk clerkFromDbByUserName = sectorMapper.findOneByClerkAccount(clerkAccount);
        if (clerkFromDbByUserName != null) {
            return ResponseResult.FAILED("该用户已经注册");
        }
        //第二步：检查邮箱格式是否正确
        String email = clerk.getClerkEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址不能为空");
        }
        if (!TextUtils.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        //第三步：检查邮箱是否已经注册
        Clerk clerkByEmail = sectorMapper.findOneByEmail(email);
        if (clerkByEmail != null) {
            return ResponseResult.FAILED("该邮箱地址已经注册");
        }
        //第四步：检查邮箱验证码是否正确
        String emailVerifyCode = (String) redisUtils.get(Constants.Clerk.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码已过期");
        }
        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        } else {
            //正确,干掉Redis里的内容
            redisUtils.del(Constants.Clerk.KEY_EMAIL_CODE_CONTENT + email);
        }
        //第五步: 检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtils.get(Constants.Clerk.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("人类验证码不正确");
        } else {
            //成功，删除验证码
            redisUtils.del(Constants.Clerk.KEY_CAPTCHA_CONTENT + captchaKey);
        }
        //达到可以注册的条件
        //第六步：对密码进行加密
        String password = clerk.getClerkPwd();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        clerk.setClerkPwd(bCryptPasswordEncoder.encode(password));
        //第七布：补全数据
        //包括：ID，注册IP，登录IP，角色、头像、创建时间、更新时间
        clerk.setClerkID(idWorker.nextId() + "");
        clerk.setClerkPhoto(Constants.Clerk.DEFAULT_PHOTO);
        clerk.setClerkStatus("1");
        //第八步：保存到数据库
        adminMapper.addClerk(clerk);
        //第九步：返回结果
        return ResponseResult.GET(ResponseStatus.JOIN_SUCCESS);
    }

    @Override
    public ResponseResult doLogin(String captcha, String captchaKey, Clerk clerk) {
        String captchaValue = (String) redisUtils.get(Constants.Clerk.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captcha.equals(captchaValue)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }
        String clerkAccount = clerk.getClerkAccount();
        if (TextUtils.isEmpty(clerkAccount)) {
            return ResponseResult.FAILED("账号不可以为空");
        }
        String password = clerk.getClerkPwd();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        //数据库
        Clerk clerkFromDb = sectorMapper.findOneByClerkAccount(clerkAccount);
        if (clerkFromDb == null) {
            return ResponseResult.FAILED("用户名或密码错误");
        }
        //验证成功，删除redis里的验证码
        redisUtils.del(Constants.Clerk.KEY_CAPTCHA_CONTENT + captchaKey);
        //用户存在
        //对比密码
        boolean matches = bCryptPasswordEncoder.matches(password, clerkFromDb.getClerkPwd());
        if (!matches) {
            return ResponseResult.FAILED("用户名或密码错误");
        }
        //密码正确
        //判断用户状态，如果是非正常的状态，则返回结果
        if (!"1".equals(clerkFromDb.getClerkStatus())) {
            return ResponseResult.ACCOUNT_DENIED();
        }
        //生成TOKEN
        createToken(getResponse(), clerkFromDb);
        return ResponseResult.SUCCESS("登录成功");
    }

    /**
     * @param response
     * @param clerkFromDb
     * @return token_key
     */
    public String createToken(HttpServletResponse response, Clerk clerkFromDb) {
        //删掉refreshToken的记录
        int deleteResult = tokenMapper.deleteAllByUserId(clerkFromDb.getClerkID());
        log.info("deleteResult of refresh token ===> " + deleteResult);
        //生成token
        Map<String, Object> claims = ClaimsUtils.clerk2Claims(clerkFromDb);
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
        String refreshTokenValue = JwtUtil.createRefreshToken(clerkFromDb.getClerkID(), Constants.TimeValueInMillions.MONTH);
        //保存到数据库里面
        //refreshToken，tokenKey,用户ID，创建时间，更新时间
        Refreshtoken refreshToken = new Refreshtoken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(clerkFromDb.getClerkID());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        //保存进数据库
        tokenMapper.insertRefreshToken(refreshToken);
        return tokenKey;
    }

    @Override
    public ResponseResult getAuthoritiesByUser(String clerkId) {
        Clerk clerk = sectorMapper.findOneById(clerkId);
        List<Authorities> authorityList = (List<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + clerk.getClerkID());
        return ResponseResult.SUCCESS("查询用户权限成功").setData(authorityList);
    }

    @Resource
    private RefreshTokenMapper tokenMapper;

    /**
     * 本质：检查用户是否有登录，如果登陆了，久返回用户信息
     *
     * @return
     */
    @Override
    public Clerk checkClerk() {
        if (clerkFromToken != null){
            return clerkFromToken;
        }
        //拿到token_key(因为之前已经CookieUtils中setUpCookie时，把tokenKey通过(Constants.User.COOKIE_TOKEN_KEY,tokenKey)保存在cookie中了)
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.Clerk.COOKIE_TOKEN_KEY);
        log.info("checkClerk tokenKey ==> " + tokenKey);
        //解析
        clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils,tokenKey);
        if (clerkFromToken == null) {
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
                String clerkId = refreshToken.getUserId();
                Clerk clerkFromDb = sectorMapper.findOneById(clerkId);
                //如果这样写，因为事务还没有提交，所以该用户的密码直接没了
                //userFromDb.setPassword("");
                //在redis里面创建新的token,因为到这里的时候，redis里面久的token已经过期自动删除了，所以这里不必再手动删除了
                String newTokenKey = createToken(getResponse(), clerkFromDb);
                //返回token
                log.info("create new token and refresh token =====");
                return TokenUtils.parseClerkByTokenKey(redisUtils,newTokenKey);
            } catch (Exception exception) {
                log.info("refresh token 已经过期 =====");
                //4、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                return null;
            }
        }
        return clerkFromToken;
    }

    @Autowired
    private Gson gson;

    @Override
    public ResponseResult findClerkInfo(String clerkId) {
        //从数据里获取
        Clerk clerkFromDb = sectorMapper.findOneById(clerkId);
        //判断结果
        if (clerkFromDb == null) {
            //如果不存在，就返回不存在
            return ResponseResult.FAILED("用户不存在");
        }
        //如果存在，就复制对象，清空密码、Email、登录ID，注册IP
        String userJson = gson.toJson(clerkFromDb);
        Clerk newClerk = gson.fromJson(userJson, Clerk.class);
        newClerk.setClerkPwd("");
        newClerk.setClerkEmail("");
        newClerk.setClerkStatus("");
        //返回结果
        return ResponseResult.SUCCESS("获取成功").setData(newClerk);
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
    public Clerk findClerkByAccount(String clerkAccount) {
        Clerk byClerkAccount = sectorMapper.findOneByClerkAccount(clerkAccount);
        return byClerkAccount;
    }

    /**
     * 表单登录的时候会调用loadUserByUsername来验证前端传过来的账号密码是否正确
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        //查角色
        Clerk byClerkAccount = sectorMapper.findOneByClerkAccount(account);
        if (byClerkAccount == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return byClerkAccount;
    }
}