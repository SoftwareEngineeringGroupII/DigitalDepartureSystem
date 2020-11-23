package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.AdminMapper;
import com.digitaldeparturesystem.mapper.AuthoritiesMapper;
import com.digitaldeparturesystem.mapper.RefreshTokenMapper;
import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Refreshtoken;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.response.ResponseState;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.*;
import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @Autowired
    private RedisUtils redisUtils;

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
     * @param type
     * @param request
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress) {
        if (emailAddress == null) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        //检查是否有初始化
        //根据类型：查询邮箱是否存在
        if ("register".equals(type) || "update".equals(type)) {
            Clerk clerkByEmail = sectorMapper.findOneByEmail(emailAddress);
            if (clerkByEmail != null) {
                return ResponseResult.FAILED("该邮箱已注册");
            }
        } else if ("forget".equals(type)) {
            Clerk clerkByEmail = sectorMapper.findOneByEmail(emailAddress);
            if (clerkByEmail == null) {
                return ResponseResult.FAILED("该邮箱已注册");
            }
        }
        //关闭sqlSession
        sqlSession.close();
        //1、防止暴力发送(不断的发送):同一个邮箱，间隔要超过30s发一次，同一个Ip，最多只能发10次(如果是短信，最多只能发5次)
        String remoteAddr = request.getRemoteAddr();
        log.info("remoteAddr ==> " + remoteAddr);
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replaceAll(":", "\\_");
        }
        //拿出来，如果没有，就通过
        log.info("Constants.Clerk.KEY_EMAIL_SEND_IP + remoteAddr ======> " + Constants.Clerk.KEY_EMAIL_SEND_IP + remoteAddr);
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
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
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
            ResponseResult.FAILED("该邮箱地址已经注册");
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
        //提交事务，关闭sqlSession
        sqlSession.commit();
        sqlSession.close();
        //第九步：返回结果
        return ResponseResult.GET(ResponseState.JOIN_SUCCESS);
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
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        Clerk clerkFromDb = sectorMapper.findOneByClerkAccount(clerkAccount);
        //关闭
        sqlSession.close();
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
    private String createToken(HttpServletResponse response, Clerk clerkFromDb) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RefreshTokenMapper tokenMapper = sqlSession.getMapper(RefreshTokenMapper.class);
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
        //把token写到cookies里
//        //创建cookies
//        Cookie cookie = new Cookie(Constants.User.COOKIE_TOKEN_KEY, tokenKey);
//        //这个要动态获取，可以从request里提取
//        cookie.setDomain("localhost");
//        cookie.setMaxAge(60 * 60 * 24 * 365);
//        cookie.setPath("/");
//        response.addCookie(cookie);
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
        tokenMapper.save(refreshToken);
        //提交
        sqlSession.commit();
        //关闭
        sqlSession.close();
        return tokenKey;
    }

    /**
     * 本质：检查用户是否有登录，如果登陆了，久返回用户信息
     *
     * @return
     */
    @Override
    public Clerk checkClerk() {
        //拿到token_key(因为之前已经CookieUtils中setUpCookie时，把tokenKey通过(Constants.User.COOKIE_TOKEN_KEY,tokenKey)保存在cookie中了)
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.Clerk.COOKIE_TOKEN_KEY);
        log.info("checkUserBean tokenKey ==> " + tokenKey);
        //解析
        Clerk clerk = parseByTokenKey(tokenKey);
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        if (clerk == null) {
            RefreshTokenMapper tokenMapper = sqlSession.getMapper(RefreshTokenMapper.class);
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
                SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
                Clerk clerkFromDb = sectorMapper.findOneById(clerkId);
                //如果这样写，因为事务还没有提交，所以该用户的密码直接没了
                //userFromDb.setPassword("");
                //在redis里面创建新的token,因为到这里的时候，redis里面久的token已经过期自动删除了，所以这里不必再手动删除了
                String newTokenKey = createToken(getResponse(), clerkFromDb);
                //返回token
                log.info("create new token and refresh token =====");
                return parseByTokenKey(newTokenKey);
            } catch (Exception exception) {
                log.info("refresh token 已经过期 =====");
                //4、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                return null;
            }finally {
                sqlSession.close();
            }
        }
        return clerk;
    }

    @Autowired
    private Gson gson;

    @Override
    public ResponseResult getClerkInfo(String clerkId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
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

    private Clerk parseByTokenKey(String tokenKey) {
        //记得加前缀，通过前面保存的(tokenKey,token)拿到token
        String token = (String) redisUtils.get(Constants.Clerk.KEY_TOKEN + tokenKey);
        log.info("parseByTokenKey token ==> " + token);
        if (token != null) {
            try {
                //说明有token，解析token
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claims2Clerk(claims);
            } catch (Exception e) {
                //过期了
                log.info("parseByTokenKey ==> " + tokenKey + " ========== 过期了");
                return null;
            }
        }
        return null;
    }

    /**
     * 表单登录的时候会调用loadUserByUsername来验证前端传过来的账号密码是否正确
     */
    @Override
    public UserDetails loadUserByUsername(String clerkAccount) throws UsernameNotFoundException {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        Clerk byClerkAccount = sectorMapper.findOneByClerkAccount(clerkAccount);
        if (byClerkAccount == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        //根据用户id查找权限
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        List<Authorities> permissions = authoritiesMapper.getRolePermissions(byClerkAccount.getClerkID());
        //创建List集合，用来保存用户菜单权限，GrantedAuthority对象代表赋予当前用户的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authorities permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        sqlSession.close();
        return new User(byClerkAccount.getClerkAccount(),byClerkAccount.getClerkPwd(),authorities);
    }


    @Override
    public Clerk findClerkByAccount(String clerkAccount) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        Clerk byClerkAccount = sectorMapper.findOneByClerkAccount(clerkAccount);
        sqlSession.close();
        return byClerkAccount;
    }
}
