package com.digitaldeparturesystem.config.security;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.CookieUtils;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    ISectorService sectorService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //TODO:解析用户上传的token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //没有令牌的key，没有登录，不往下执行
        Clerk clerkFromToken = null;
        if (!TextUtils.isEmpty(tokenKey)) {
            //从用户的cookies中获取到了token，再把token解析成了Clerk对象
            clerkFromToken = sectorService.checkClerk();
        }
//        UserDetails userByUserAccount = sectorService.loadUserByUsername(clerkFromToken.getClerkAccount());
        if (clerkFromToken != null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(clerkFromToken, null, clerkFromToken.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
