package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.ProcessMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.service.IProcessService;
import com.digitaldeparturesystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Set;

import static com.digitaldeparturesystem.utils.TextUtils.isEmpty;

@Service
@Transactional
public class ProcessServiceImpl implements IProcessService {
    @Autowired
    private IdWorker idWorker;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private RedisUtils redisUtils;
    /**
     * 显示离校进度审核状况
     * @param
     * @return
     */
    @Override
    public ResponseResult showProcess() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        Process process = processMapper.showProcess(student.getStuId());
        return ResponseResult.SUCCESS("显示成功！").setData(new ArrayList<Process>(){{add(process);}});
    }

    //仅供测试
    @Override
    public ResponseResult addProcess(Process process) {
        return null;
    }


    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
