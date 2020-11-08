package com.digitaldeparturesystem.controller;

import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return responseResult;
    }

}
