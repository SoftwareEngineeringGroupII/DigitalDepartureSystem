package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.LogSearchCondition;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICommonService;
import com.digitaldeparturesystem.service.ILogcatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonApi {

    @Resource
    private ICommonService commonService;

    @Resource
    private ILogcatService logcatService;

    @Test
    public void sendVerify(){
//        System.out.println(commonService.sendEmail("forget", getRequest(), "1584677103@qq.com").getData());
    }

    @Test
    public void updatePwd(){
        //发送验证码
//        System.out.println(commonService.updateUserPassword("").getData());
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    @Test
    public void getLogs(){
        LogSearchCondition condition = new LogSearchCondition();
        condition.setKeyWord("debug");
        ResponseResult logs = logcatService.getLogs("2018110427", condition);
        System.out.println(logs.getData());
    }

}
