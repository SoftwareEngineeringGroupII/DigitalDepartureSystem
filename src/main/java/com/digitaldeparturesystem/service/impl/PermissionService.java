package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.UserRoleMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.CookieUtils;
import com.digitaldeparturesystem.utils.MybatisUtils;
import com.digitaldeparturesystem.utils.TextUtils;
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

@Service("permission")
public class PermissionService {

    @Autowired
    private ISectorService sectorService;

    /**
     * 权限判断
     * @param request
     * @param authentication
     * @return
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        
        Object userInfo = authentication.getPrincipal();
        boolean hasPermission  = false;
        if (userInfo instanceof UserDetails) {
            Clerk clerk = (Clerk) userInfo;
            //获取资源
            Set<String> urls = new HashSet();
            //查url
            if (clerk.getAuthorities() != null){
                for (GrantedAuthority authority : clerk.getAuthorities()) {
                    urls.add(authority.getAuthority());
                }
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
