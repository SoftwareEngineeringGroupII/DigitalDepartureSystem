package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.UserRoleMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service("permission")
public class PermissionService {

    @Autowired
    private ISectorService sectorService;

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
        //解析*
        Clerk clerk = TokenUtils.parseByTokenKey(redisUtils,tokenKey);
        if (clerk == null){
            clerk = sectorService.checkClerk();
        }
        boolean hasPermission = false;
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
        } else {
            return false;
        }
    }

}
