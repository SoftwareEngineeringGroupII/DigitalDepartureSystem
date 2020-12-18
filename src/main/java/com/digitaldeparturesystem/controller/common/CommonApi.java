package com.digitaldeparturesystem.controller.common;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.LogSearchCondition;
import com.digitaldeparturesystem.pojo.PasswordBean;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICommonService;
import com.digitaldeparturesystem.service.ILogcatService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.CookieUtils;
import com.digitaldeparturesystem.utils.RedisUtils;
import com.digitaldeparturesystem.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonApi {

    @Autowired
    private ICommonService commonService;

    @Resource
    private ISectorService sectorService;

    @Resource
    private IStudentService studentService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private ILogcatService logcatService;

    @GetMapping("/menu")
    public ResponseResult getMenu(){
        return commonService.getMenu();
    }

    @GetMapping("/login")
    public ResponseResult login() {
        return ResponseResult.ACCOUNT_NOT_LOGIN();
    }

    @GetMapping("/login/error")
    public ResponseResult loginError(){
        return ResponseResult.LOGIN_FAILED();
    }

    @GetMapping("/index")
    public ResponseResult index(){
        return ResponseResult.SUCCESS("登录成功");
    }

    @GetMapping("/info")
    public ResponseResult getUserInfo(HttpServletRequest httpServletRequest){
        return commonService.getUserInfo();
    }

    /**
     * 修改密码
     * 修改密码、找回密码
     * 普通做法：通过旧密码对比来更新密码
     *
     * 找回密码：发送验证码到邮箱/手机 ---> 判断验证码是否正确来判断对应邮箱/手机号码所注册的账号是否属于你
     *
     * 步骤：
     * 1、用户填写邮箱
     * 2、用户获取验证码type=forget
     * 3、填写验证码
     * 4、填写新的密码
     * 5、提交数据
     *
     * 数据包括：
     * 1、邮箱和新密码
     * 2、验证码
     *
     * 如果验证码正确 ---> 所有邮箱注册的账号就是你得，可以修改密码
     * localhost:8888/user/password/57400
     * @return
     */
    @PutMapping("/recoveredPwd/{userAccount}/{verifyCode}")
    public ResponseResult recoveredUserPassword(@PathVariable("verifyCode")String verifyCode, @RequestBody String password,@PathVariable("userAccount")String userAccount) {
        return commonService.recoveredUserPassword(verifyCode,password,userAccount);
    }

    @PutMapping("/updatePwd/{captcha}/{captcha_key}")
    public ResponseResult updateUserPassword(@PathVariable("captcha_key") String captchaKey,
                                             @PathVariable("captcha") String captcha,
                                             @RequestBody PasswordBean passwordBean) {
        return commonService.updateUserPassword(captcha, captchaKey,passwordBean);
    }

    @PutMapping("/updatePwd")
    public ResponseResult updateUserPassword(@RequestBody PasswordBean passwordBean) {
        return commonService.updateUserPassword(passwordBean);
    }

    /**
     * 获取图灵验证码
     * 10分钟有效期
     * @return
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key")String captchaKey){
        try {
            commonService.createCaptcha(response,captchaKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    /**
     * 发送邮件email
     *
     * localhost:8888/user/verify_code?type=forget&email=1584677103@qq.com
     * @return
     */
    @GetMapping("/sendEmail")
    public ResponseResult sendVerifyCode(HttpServletRequest request,
                                         @RequestParam("email") String emailAddress) {
        log.info("email ==> " + emailAddress);
        return commonService.sendEmail(request,emailAddress);
    }

    @GetMapping("/logs")
    public ResponseResult getLogs(HttpServletRequest request, @RequestBody LogSearchCondition condition){
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            //不为职员
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            if (student == null){//不是学生
                return ResponseResult.FAILED("没有改用户的日志信息");
            }
            return logcatService.getLogs(student.getStuNumber(),condition);
        }else{
            //是职员
            return logcatService.getLogs(clerkFromToken.getClerkAccount(),condition);
        }
    }

}
