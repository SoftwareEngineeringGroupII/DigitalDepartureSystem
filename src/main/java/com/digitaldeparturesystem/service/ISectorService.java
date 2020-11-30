package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface ISectorService extends UserDetailsService {

    void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception;

    ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress);

    ResponseResult register(Clerk clerk, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request);

    ResponseResult doLogin(String captcha, String captchaKey, Clerk clerk);

    Clerk checkClerk();

    ResponseResult findClerkInfo(String clerkId);

    //---一卡通管理员---//

    ResponseResult getStuInfo(String stuId);

    Clerk findClerkByAccount(String clerkAccount);

    String createToken(HttpServletResponse httpServletResponse, Clerk clerk);

    /**
     * 获取用户所拥有的权限
     * @param clerkId
     * @return
     */
    ResponseResult getAuthoritiesByUser(String clerkId);

}
