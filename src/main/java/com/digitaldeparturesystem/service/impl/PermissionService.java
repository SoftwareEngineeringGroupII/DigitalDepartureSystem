package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
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
    private AntPathMatcher antPathMatcher;

    /**
     * 权限判断
     * @param request
     * @param authentication
     * @return
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        if (antPathMatcher == null){
            antPathMatcher = new AntPathMatcher();
        }
        String requestURI = request.getRequestURI();
        if (antPathMatcher.match(Constants.CommonApi.RECOVERED_PWD, requestURI)
        ||antPathMatcher.match(Constants.CommonApi.SEND_EMAIL,requestURI)){
            return true;
        }
        boolean hasPermission = false;
        //用用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            if (student == null){
                return false;
            }
            //记录
            log.info(IpUtil.getIpAddr(request) + " -- " + student.getStuNumber() + " -- " + request.getRequestURI());
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            Set<String> urls = new HashSet();
            //查url
            urls.add("/student/**");
            urls.add("/common/**");
            urls.add("/login/**");
            urls.add("/logout/**");
            urls.add("/**");
            for (String url : urls) {
                if (antPathMatcher.match(url, requestURI)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission){
                log.info(student.getStuNumber() + " 权限不足，访问 -- " + request.getRequestURI() + " -- 被拒绝");
            }
            return hasPermission;
        }else{
            //记录
            log.info(IpUtil.getIpAddr(request) + " -- " + clerkFromToken.getClerkAccount() + " -- " + request.getRequestURI());
            //从redis里面拿到权限
            Set<Authorities> authorityList = (Set<Authorities>) redisUtils.get(Constants.Clerk.KEY_AUTHORITY_CONTENT + clerkFromToken.getClerkID());
            //获取资源
            Set<String> urls = new HashSet();
            //查url
            for (Authorities authorities : authorityList) {
                urls.add(authorities.getUrl()+"/**");
            }
            urls.add("/common/**");
            urls.add("/login/**");
            urls.add("/logout/**");
          urls.add("/**");
            //不需要权限，都可以访问
//            //TODO:
//            urls.add("/**");
            //匹配url，看是否可以访问此url
            for (String url : urls) {
                if (antPathMatcher.match(url, requestURI)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission){
                log.info(clerkFromToken.getClerkAccount() + " 权限不足，访问 -- " + request.getRequestURI() + " -- 被拒绝");
            }
            return hasPermission;
        }
    }

}
