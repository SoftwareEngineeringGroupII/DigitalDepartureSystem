package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.CheckcommitMapper;
import com.digitaldeparturesystem.mapper.RoleMapper;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICheckcommitService;
import com.digitaldeparturesystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("ICheckcommitService")
public class CheckServiceImpl implements ICheckcommitService {
    /**
     * 学生提交审核
     */
    @Resource
    private IdWorker idWorker;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private CheckcommitMapper checkcommitMapper;
    @Override
    public ResponseResult addCheckCommit(Checkcommit checkcommit) {
        if (TextUtils.isEmpty(checkcommit.getReason())) {
            return ResponseResult.FAILED("审核理由不能为空!");
        }
        if (TextUtils.isEmpty(checkcommit.getType())) {
            return ResponseResult.FAILED("审核类型不能为空!");
        }
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);

        //assert student != null;
        Checkcommit newcheck = new Checkcommit();
        if (TextUtils.isEmpty(checkcommit.getType())) {
            return ResponseResult.FAILED("审核类型不能为空！");
        }
        if (TextUtils.isEmpty(checkcommit.getReason())){
            return ResponseResult.FAILED("审核内容不能为空！");
        }
        checkcommit.setCommitdate(new Date());
        checkcommit.setCheckId(idWorker.nextId()+"");
//        checkcommit.setType(newcheck.getType());
        checkcommit.setStuId(student.getStuId());
        //保存数据
        checkcommitMapper.addCheckCommit(checkcommit);
        return ResponseResult.SUCCESS("提交审核成功");
    }
    @Override
    public ResponseResult showCheckCommit() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        List<Checkcommit> commits = checkcommitMapper.showCheckCommit();
        return ResponseResult.SUCCESS("显示成功！").setData(commits);
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
