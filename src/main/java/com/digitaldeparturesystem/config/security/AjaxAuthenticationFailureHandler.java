package com.digitaldeparturesystem.config.security;

import com.alibaba.fastjson.JSON;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info(IpUtil.getIpAddr(httpServletRequest) + " ==> login failure" );
        httpServletResponse.setHeader("Content-type","text/html;charset=UTF-8");//设置相遇类型为html,编码为utf-8,处理相应页面显示的乱码
        httpServletResponse.setCharacterEncoding("UTF-8");//如果响应类型为文本,那么就需要设置文本的编码类型,然后浏览器使用这个编码来解读文本

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8085");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");

        httpServletResponse.getWriter().write(JSON.toJSONString(ResponseResult.LOGIN_FAILED()));
    }
}
