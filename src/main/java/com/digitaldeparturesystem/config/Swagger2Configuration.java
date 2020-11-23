package com.digitaldeparturesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 配置文件
 */
@Configuration
public class Swagger2Configuration {

    //版本
    public static final String VERSION = "1.0.0";

    /**
     * 学生api，接口前缀：student
     *
     * @return
     */
    @Bean
    public Docket studentApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(studentApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.digitaldeparturesystem.controller.student"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("学生中心");
    }

    private ApiInfo studentApiInfo() {
        return new ApiInfoBuilder()
                .title("数字离校系统学生接口文档") //设置文档的标题
                .description("学生接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }


    /**
     * 管理中心api，接口前缀：admin
     *
     * @return
     */
    @Bean
    public Docket adminApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.digitaldeparturesystem.controller.admin"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("管理中心");
    }


    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("数字离校系统接口文档") //设置文档的标题
                .description("管理员接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }


    @Bean
    public Docket sectionApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(sectionApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.digitaldeparturesystem.controller.sector"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("部门中心");
    }

    private ApiInfo sectionApiInfo() {
        return new ApiInfoBuilder()
                .title("数字离校系统接口文档") //设置文档的标题
                .description("部门接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

}

