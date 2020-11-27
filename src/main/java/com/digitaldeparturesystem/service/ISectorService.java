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

    ResponseResult getClerkInfo(String clerkId);

    //---一卡通管理员---//

    //上传公告
    ResponseResult uploadNotice(Notice notice, MultipartFile photo) throws IOException;

    //获取全部学生信息
    public ResponseResult listStuAll(int page, int size, HttpServletRequest request, HttpServletResponse response);

    ResponseResult getStuInfo(String stuId);

    Clerk findClerkByAccount(String clerkAccount);

    //查询所有学生
    ResponseResult findAllByPage(Map<String,Object> map);

    //根据学院类型、学生类型、审核状态分页查询
    ResponseResult findAllByPageAndType(Map<String,Object> map);

    //根据学号查询学生一卡通详情
    ResponseResult getStudentByIdForCard(String studentId);

    //审核学生一卡通,修改其审核、余额状态
    ResponseResult doCheckForCard(String stuNumber);



}
