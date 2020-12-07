package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service("permission")
public class PermissionService {

    @Autowired
    private ISectorService sectorService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 权限判断
     * @param request
     * @param authentication
     * @return
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //用用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = null;
        //解析管理员
        try {
            if (tokenKey == null){
                clerk = sectorService.checkClerk();
            }else {
                clerk = TokenUtils.parseClerkByTokenKey(redisUtils,tokenKey);
            }
            if (clerk.getClerkID() == null){
                clerk = null;
            }
        } catch (Exception e) {
            clerk = null;
        }
        boolean hasPermission = false;
        //管理员权限
        if (clerk != null) {
            //记录
            log.info(IpUtil.getIpAddr(request) + " -- " + clerk.getClerkAccount() + " -- " + request.getRequestURI());
            //从redis里面拿到权限
            Set<Authorities> authorityList = (Set<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + clerk.getClerkID());
            //获取资源
            Set<String> urls = new HashSet();
            //查url
            for (Authorities authorities : authorityList) {
                urls.add(authorities.getUrl());
            }
            //不需要权限，都可以访问
            //TODO:
            urls.add("/**");

            AntPathMatcher antPathMatcher = new AntPathMatcher();
            //匹配url，看是否可以访问此url
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
            return hasPermission;
        }
        //解析学生
        Student student = null;
        try {
            if (tokenKey == null){
                student = studentService.checkStudent();
            }else {
                student = TokenUtils.parseStudentByTokenKey(redisUtils,tokenKey);
            }
        } catch (Exception e) {

        }
        //学生权限
        if (student != null){
            //记录
            log.info(IpUtil.getIpAddr(request) + " -- " + student.getStuNumber() + " -- " + request.getRequestURI());
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            return antPathMatcher.match("/**", request.getRequestURI());
        }
        return false;
    }

}
