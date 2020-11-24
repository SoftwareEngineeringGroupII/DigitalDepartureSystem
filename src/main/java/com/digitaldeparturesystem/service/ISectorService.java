package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ISectorService {

    void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception;

    ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress);

    ResponseResult register(Clerk clerk, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request);

    ResponseResult doLogin(String captcha, String captchaKey, Clerk clerk);

    Clerk checkClerk();

    ResponseResult getClerkInfo(String clerkId);

    //上传公告
    ResponseResult uploadNotice(Notice notice, MultipartFile photo, HttpServletRequest request, HttpServletResponse response) throws IOException;

    //获取全部学生信息
    public ResponseResult listStuAll(int page, int size, HttpServletRequest request, HttpServletResponse response);

    ResponseResult getStuInfo(String stuId);
}
