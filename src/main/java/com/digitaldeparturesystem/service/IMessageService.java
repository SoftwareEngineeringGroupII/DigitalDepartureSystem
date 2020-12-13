package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IMessageService {
    //发布信息
    ResponseResult sendMessage(Message message);
    //删除信息
    ResponseResult deleteMessage(String messageID);
    //回复信息
    ResponseResult responseMessage(String messageID);
}
