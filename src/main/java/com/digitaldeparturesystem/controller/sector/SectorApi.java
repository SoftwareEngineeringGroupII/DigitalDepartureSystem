package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/sector")
public class SectorApi {

    @Autowired
    private ISectorService sectorService;

    /**
     * 注册
     * @return
     */
    @PreAuthorize("@permission.sector()")
    @PostMapping("/join_in")
    public ResponseResult register(@RequestBody Clerk clerk,
                                   @RequestParam("email_code")String emailCode,
                                   @RequestParam("captcha_code")String captchaCode,
                                   @RequestParam("captcha_key")String captchaKey,
                                   HttpServletRequest request) {
        return sectorService.register(clerk,emailCode,captchaCode,captchaKey,request);
    }

    /**
     * 登录sign-up
     * <p>
     * 需要提交的数据
     * 1、用户账号-可以昵称，可以邮箱--------->做了为已处理
     * 2、密码
     * 3、图灵验证码
     * 4、图灵验证码的key
     * </p>
     *
     * @param captchaKey 图灵验证码的key
     * @param captcha    图灵验证码
     * @param clerk   用户bean类，封装账号和密码
     * @return
     */
    @PostMapping("/login/{captcha}/{captcha_key}")
    public ResponseResult login(@PathVariable("captcha_key") String captchaKey,
                                @PathVariable("captcha") String captcha,
                                @RequestBody Clerk clerk) {
        return sectorService.doLogin(captcha, captchaKey, clerk);
    }

    @GetMapping("/login")
    public ResponseResult login() {
        return ResponseResult.ACCOUNT_NOT_LOGIN();
    }

    /**
     * 获取图灵验证码
     * 10分钟有效期
     * @return
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key")String captchaKey){
        try {
            sectorService.createCaptcha(response,captchaKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    /**
     * 发送邮件email
     *
     * 使用场景：注册、找回密码、修改邮箱(会输入新的邮箱）
     * 注册：如果已经注册过了，就提示该邮箱已经注册
     * 找回密码：如果没有注册过，提示该邮箱没有注册
     * 修改邮箱（新的邮箱）：如果已经注册了，提示该邮箱已经注册
     * localhost:8888/user/verify_code?type=forget&email=1584677103@qq.com
     * @param type 用于表示用户的行为
     * @return
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(HttpServletRequest request, @RequestParam("type")String type,
                                         @RequestParam("email") String emailAddress) {
        log.info("email ==> " + emailAddress);
        return sectorService.sendEmail(type,request,emailAddress);
    }


    /**
     * 修改密码
     * @return
     */
    @PreAuthorize("@permission.sector()")
    @PostMapping("/password")
    public ResponseResult updatePassword(@RequestBody Clerk clerk){
        return null;
    }


    /**
     * 获取用户信息
     * @return
     */
    @PreAuthorize("@permission.sector()")
    @GetMapping("/{clerkId}")
    public ResponseResult getStudentInfo(@PathVariable("clerkId")String clerkId){
        return null;
    }

    /**
     * 修改用户信息user-info
     * @return
     */
    @PreAuthorize("@permission.sector()")
    @PutMapping
    public ResponseResult updateStuInfo(@RequestBody Clerk clerk){
        return null;
    }
}
