package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.utils.EmailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    /**
     * 调用的地方和声明的地方不能在同一个类，不然就没有异步的效果
     * @param verifyCode
     * @param emailAddress
     * @throws Exception
     */
    @Async
    public void sendEmailVerifyCode(String verifyCode,String emailAddress) throws Exception{
        EmailSender.sendRegisterVerifyCode(verifyCode,emailAddress);
    }

}
