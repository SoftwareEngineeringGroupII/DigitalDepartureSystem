package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.DetailMapper;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDetailsService;
import com.digitaldeparturesystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
@Transactional
public class DetailsServiceImpl implements IDetailsService {
    @Resource
    private DetailMapper detailMapper;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 学生信息显示
     * @param
     * @return
     */

    @Override
    public ResponseResult showDetails() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        Student stu = detailMapper.showDetails(student.getStuId());
        return ResponseResult.SUCCESS("显示成功！").setData(new ArrayList<Student>(){{add(stu);}});
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
