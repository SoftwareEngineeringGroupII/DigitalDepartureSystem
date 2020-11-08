package com.digitaldeparturesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan("com.digitaldeparturesystem.mapper")//指定扫描接口与映射配置文件的包名
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
