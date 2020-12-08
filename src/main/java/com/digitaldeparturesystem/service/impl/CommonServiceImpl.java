package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICommonService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.CookieUtils;
import com.digitaldeparturesystem.utils.RedisUtils;
import com.digitaldeparturesystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ResponseResult getMenu() {
        HttpServletRequest request = getRequest();
        //用用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        //权限信息
        Set<Authorities> authorityFromDB;
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            authorityFromDB = (Set<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + student.getStuId());
        }else{
            authorityFromDB = (Set<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + clerkFromToken.getClerkID());
        }
        //权限
        Set<Authorities> authorityList = new TreeSet<>(new Comparator<Authorities>() {
            @Override
            public int compare(Authorities o1, Authorities o2) {
                return (int)(o1.getIndex() - o2.getIndex());
            }
        });
        for (Authorities authorities : authorityFromDB) {
            authorityList.add(authorities);
        }
        return ResponseResult.SUCCESS("获取菜单成功").setData(authorityList);
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
