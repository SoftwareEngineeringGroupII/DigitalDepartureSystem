package com.digitaldeparturesystem.mapper;
import com.digitaldeparturesystem.utils.EmailSender;

import javax.mail.MessagingException;

public class TestEmailSender {

    public static void main(String[] args) throws MessagingException {
        EmailSender.subject("测试邮件发送")
                .from("个人博客系统")
                .text("发送的内容: ab12rf")
                .to("1584677103@qq.com")
                .send();
    }

}
