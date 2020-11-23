package com.digitaldeparturesystem.config;

import com.digitaldeparturesystem.service.ISectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * RBAC: role-base-access-control
 * 三张表：
 * 用户表 --> 角色 --> 权限(对应的就是API的访问)
 * 角色表
 * 权限表
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启prePost
public class WebSpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ISectorService sectorService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 用户认证操作
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //添加用户，并给予权限
//        auth.inMemoryAuthentication().withUser("aaa").password(bCryptPasswordEncoder.encode("1234")).roles("ADMIN");
        auth.inMemoryAuthentication().withUser("aaa").password("1234").roles("ADMIN");
        auth.userDetailsService(sectorService).passwordEncoder(bCryptPasswordEncoder);
    }


    /**
     * 用户授权操作
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http.csrf().disable();    //安全器令牌
//        http.formLogin()
//
//                //登录请求被拦截
//                .loginPage("/sector/login").permitAll()
//                //设置默认登录成功跳转页面
//                .successForwardUrl("/main")
//                .failureUrl("/login?error");   //登录失败的页面
        http.authorizeRequests().antMatchers("/**").permitAll();
//        http.authorizeRequests().antMatchers("/static/**", "/assets/**").permitAll();    //文件下的所有都能访问
//        http.authorizeRequests().antMatchers("/webjars/**").permitAll();
//        http.logout().logoutUrl("/logout").permitAll();     //退出
        http.authorizeRequests().anyRequest().authenticated();    //除此之外的都必须通过请求验证才能访问
    }

}
