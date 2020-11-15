package com.digitaldeparturesystem;

import com.digitaldeparturesystem.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
}
