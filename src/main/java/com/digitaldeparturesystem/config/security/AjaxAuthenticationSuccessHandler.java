package com.digitaldeparturesystem.config.security;

import com.alibaba.fastjson.JSON;
import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.UserRoleMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ISectorService sectorService;

    @Autowired
    private IStudentService studentService;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 登录流程：
     * 用户登录成功后，会去loadUserByUsername方法里面检验我们的账号和密码是否正确
     *
     * 如果正确，就会调用到这里来，我们就能获取用户的IP、UserName等等
     * 并且在这里，会缓存用户的权限信息到Redis里面，方便后续的调用
     * 然后，会去调用hasPermission()方法，去审核用户是否有该权限，我们就能在那里去获取，用户的行为
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-type","text/html;charset=UTF-8");//设置相遇类型为html,编码为utf-8,处理相应页面显示的乱码
        httpServletResponse.setCharacterEncoding("UTF-8");//如果响应类型为文本,那么就需要设置文本的编码类型,然后浏览器使用这个编码来解读文本

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8085");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");


        Object principal = authentication.getPrincipal();
        Clerk clerk = null;
        if (principal instanceof Clerk){
            clerk = (Clerk)principal;
            log.info(IpUtil.getIpAddr(httpServletRequest) + " ==> " + clerk.getClerkAccount() +" ==> login success" );
            //登录成功
            log.info("管理员账户名 ==> " + clerk.getClerkAccount());
        }
        Student student = null;
        if (principal instanceof Student){
            student = (Student) principal;
            log.info(IpUtil.getIpAddr(httpServletRequest) + " ==> " + student.getStuNumber() +" ==> login success" );
            //登录成功
            log.info("学生账户名 ==> " + student.getStuNumber());
        }

        String token = "";
        if (clerk != null){
            //获取用户权限
            getUserAuthority(clerk.getClerkID());
            //获取用户token
            String tokenKey = CookieUtils.getCookie(httpServletRequest, Constants.Clerk.COOKIE_TOKEN_KEY);
            Clerk clerkFromToken = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
            //如果这里解析不出来，说明用户的token过期了，那么根据refreshToken，再给用户创建一个Token
            //或者更换了用户
            if (clerkFromToken == null||clerk.getClerkID() != clerkFromToken.getClerkID()){
                //删除旧的refreshToken，创建新的refreshToken
                token = sectorService.createToken(httpServletResponse, clerk);
            }
            //解决不返回token的问题
            if ("".equals(token)){
                httpServletResponse.getWriter().
                        write(JSON.toJSONString(ResponseResult.SUCCESS("登录成功").
                                setData(JSON.toJSON(clerkFromToken)).setToken(tokenKey)));
            }else{
                httpServletResponse.getWriter().
                        write(JSON.toJSONString(ResponseResult.SUCCESS("登录成功").
                                setData(JSON.toJSON(clerk)).setToken(token)));
            }
        }
        if(student != null){
            //获取用户的权限
            getUserAuthority(student.getStuId());
            //获取用户token
            String tokenKey = CookieUtils.getCookie(httpServletRequest, Constants.Clerk.COOKIE_TOKEN_KEY);
            Student studentFromToken = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
            //如果这里解析不出来，说明用户的token过期了，那么根据refreshToken，再给用户创建一个Token
            if (studentFromToken == null||student.getStuId() != studentFromToken.getStuId()){
                //删除旧的refreshToken，创建新的refreshToken
                token = studentService.createToken(httpServletResponse, student);
            }
            //解决不返回token的问题
            if ("".equals(token)){
                httpServletResponse.getWriter().
                        write(JSON.toJSONString(ResponseResult.SUCCESS("登录成功").
                                setData(JSON.toJSON(studentFromToken)).setToken(tokenKey)));
            }else{
                httpServletResponse.getWriter().
                        write(JSON.toJSONString(ResponseResult.SUCCESS("登录成功").
                                setData(JSON.toJSON(student)).setToken(token)));
            }
        }

    }

    private void getUserAuthority(String userId) {
        //获取用户的权限
        List<Role> roles;
        roles = userRoleMapper.getRolesByUser(userId);
        //创建List集合，用来保存用户菜单权限，GrantedAuthority对象代表赋予当前用户的权限
        Set<Authorities> authorityList = new HashSet<>();
        //查权限
        for (Role role : roles) {
            List<Authorities> authorities = roleAuthorityMapper.getAuthorityNoParentByRole(role.getId());
            List<String> authorityIds = roleAuthorityMapper.getRoleAuthorityData(role.getId());
            for (Authorities authority : authorities) {
                AuthorityTreeUtils.getSpecialChildrenToMenu(roleAuthorityMapper, authority,authorityIds);
                //添加权限
                authorityList.add(authority);
            }
        }
        //权限保存进Redis
        redisUtils.set(Constants.Clerk.KEY_AUTHORITY_CONTENT + userId, authorityList, Constants.TimeValueInSecond.HOUR_2);
    }
}

