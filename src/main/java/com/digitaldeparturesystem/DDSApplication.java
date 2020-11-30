package com.digitaldeparturesystem;

import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.RedisUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.util.Random;

@Slf4j
@EnableSwagger2
@SpringBootApplication()
@MapperScan("com.digitaldeparturesystem.mapper")//指定扫描接口与映射配置文件的包名，只能扫描工程目录下的mapper
public class DDSApplication {

    public static void main(String[] args) {
        log.info("DDSApplication is running...");
        SpringApplication.run(DDSApplication.class,args);
    }

    //不加这个声明，后面不能注入
    @Bean
    public IdWorker createIdWorker(){
        return new IdWorker(0,0);
    }

    //加密密码
    @Bean
    public BCryptPasswordEncoder createPasswordEncoder(){
        //不加盐值，直接用它默认的
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisUtils createRedisUtils(){
        return new RedisUtils();
    }

    @Bean
    public Random createRandom(){
        return new Random();
    }

    @Bean
    public Gson createGson(){
        return new Gson();
    }



}
