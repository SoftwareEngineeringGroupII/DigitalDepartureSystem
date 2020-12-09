package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.service.ICommonService;
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

}
