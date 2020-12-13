package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.CheckcommitMapper;
import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICheckcommitService;
import com.digitaldeparturesystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CheckServiceImpl implements ICheckcommitService {
    /**
     * 学生提交审核
     */
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private CheckcommitMapper checkcommitMapper;
    @Override
    public ResponseResult addCheckCommit(Checkcommit checkcommit) {
        try {
            if (TextUtils.isEmpty(checkcommit.getStuId())) {
                return ResponseResult.FAILED("学号不能为空!");
            }
            if (TextUtils.isEmpty(checkcommit.getReason())) {
                return ResponseResult.FAILED("审核理由不能为空!");
            }
            if (TextUtils.isEmpty(checkcommit.getType())) {
                return ResponseResult.FAILED("审核类型不能为空!");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            //补充数据
            checkcommit.setCheckId(String.valueOf(idWorker.nextId()));
            //获取提交审核时间
            checkcommit.setCommitdate(date);

            HttpServletRequest request = getRequest();
            //用户cookies里面获取token
            String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
            //解析*
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            assert student != null;
            //获取审核原因
            checkcommit.setReason(checkcommit.getReason());
            checkcommitMapper.saveCheckcommit(checkcommit);
            return ResponseResult.SUCCESS("提交成功！");
        }catch (Exception e){
            return ResponseResult.FAILED("提交失败！");
        }
    }
    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
