package com.digitaldeparturesystem.controller.common;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICommonService;
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

    @GetMapping("/menu")
    public ResponseResult getMenu(){
        return commonService.getMenu();
    }

    @GetMapping("/login")
    public ResponseResult login() {
        return ResponseResult.ACCOUNT_NOT_LOGIN();
    }

    @GetMapping("/info")
    public ResponseResult getUserInfo(HttpServletRequest httpServletRequest){
        //获取用户token
        String tokenKey = CookieUtils.getCookie(httpServletRequest, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            return ResponseResult.SUCCESS("查询用户信息成功").setData(student);
        }else{
            return ResponseResult.SUCCESS("查询用户信息成功").setData(clerkFromToken);
        }
    }

}
