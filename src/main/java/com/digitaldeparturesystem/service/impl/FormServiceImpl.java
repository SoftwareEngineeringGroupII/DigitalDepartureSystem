package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.DetailMapper;
import com.digitaldeparturesystem.mapper.FormMapper;
import com.digitaldeparturesystem.mapper.ProcessMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Form;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFormService;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import static com.digitaldeparturesystem.utils.TextUtils.isEmpty;

@Service
@Transactional
public class FormServiceImpl implements IFormService {
    /**
     * 离校表单显示
     */
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private FormMapper formMapper;
    @Resource
    private IdWorker idWorker;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ProcessMapper processMapper;
    @Resource
    private StudentMapper studentMapper;

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    @Override
    public ResponseResult showFormByStudent() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        Student stu = detailMapper.showDetails(student.getStuId());
        return ResponseResult.SUCCESS("显示成功！").setData(new ArrayList<Student>(){{add(stu);}});
    }

    @Override
    public ResponseResult showFormByProcess() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        Process process = processMapper.showProcess(student.getStuId());
        return ResponseResult.SUCCESS("显示成功！").setData(new ArrayList<Process>(){{add(process);}});
    }
}
