package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector")
public class SectorApi {

    @Autowired
    private ISectorService sectorService;

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
    @PostMapping("/login/{captcha_key}/{captcha}")
    public ResponseResult login(@PathVariable("captcha_key") String captchaKey,
                                @PathVariable("captcha") String captcha,
                                @RequestBody Clerk clerk) {
        return sectorService.doLogin(captcha, captchaKey, clerk);
    }

}
