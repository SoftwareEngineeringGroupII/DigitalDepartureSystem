package com.digitaldeparturesystem;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@SpringBootApplication()
@MapperScan("com.digitaldeparturesystem.mapper")//指定扫描接口与映射配置文件的包名
public class DDSApplication {

    public static void main(String[] args) {
        log.info("DDSApplication is running...");
        SpringApplication.run(DDSApplication.class,args);
    }
}
