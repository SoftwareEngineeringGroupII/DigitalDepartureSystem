package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ISectorService extends UserDetailsService {

    void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception;

    ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress);

    ResponseResult register(Clerk clerk, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request);

    ResponseResult doLogin(String captcha, String captchaKey, Clerk clerk);

    Clerk checkClerk();

    ResponseResult findClerkInfo(String clerkId);

    Clerk findClerkByAccount(String clerkAccount);

    String createToken(HttpServletResponse httpServletResponse, Clerk clerk);

    /**
     * 获取用户所拥有的权限
     * @param clerkId
     * @return
     */
    ResponseResult getAuthoritiesByUser(String clerkId);
}
