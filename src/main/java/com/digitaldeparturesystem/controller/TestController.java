package com.digitaldeparturesystem.controller;

import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello_world")
    public ResponseResult helloWorld(){
        System.out.println("hello world");
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setCode(20000);
        responseResult.setMessage("操作成功");
        responseResult.setData("hello world");
        log.info("hello world!!!");
        return responseResult;
    }

}
