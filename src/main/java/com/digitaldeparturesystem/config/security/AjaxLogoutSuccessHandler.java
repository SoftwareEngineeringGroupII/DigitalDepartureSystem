package com.digitaldeparturesystem.config.security;

import com.alibaba.fastjson.JSON;
import com.digitaldeparturesystem.mapper.RefreshTokenMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RefreshTokenMapper tokenMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8085");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");

        log.info(IpUtil.getIpAddr(httpServletRequest) + " ==> logout success");
        //退出成功，删除数据库的refreshToken和删除redis的token
        String tokenKey = CookieUtils.getCookie(httpServletRequest, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
        //删除refreshToken
        if (clerkFromToken == null||clerkFromToken.getClerkID() == null){
            Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            tokenMapper.deleteAllByUserId(student.getStuId());
            //删除用户权限
            redisUtils.del(Constants.Clerk.KEY_AUTHORITY_CONTENT + student.getStuId());
        }else{
            tokenMapper.deleteAllByUserId(clerkFromToken.getClerkID());
            //删除用户权限
            redisUtils.del(Constants.Clerk.KEY_AUTHORITY_CONTENT + clerkFromToken.getClerkID());
        }
        //删除redis中的token
        redisUtils.del(Constants.Clerk.KEY_TOKEN + tokenKey);
        //删除本地cookie
        CookieUtils.deleteCookie(httpServletResponse,Constants.Clerk.COOKIE_TOKEN_KEY);

        //删掉refreshToken的记录
        httpServletResponse.setHeader("Content-type","text/html;charset=UTF-8");//设置相遇类型为html,编码为utf-8,处理相应页面显示的乱码
        httpServletResponse.setCharacterEncoding("UTF-8");//如果响应类型为文本,那么就需要设置文本的编码类型,然后浏览器使用这个编码来解读文本
        httpServletResponse.getWriter().write(JSON.toJSONString(ResponseResult.LOGOUT_SUCCESS()));
    }
}
