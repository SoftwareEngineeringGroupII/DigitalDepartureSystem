package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IMessageService {
    ResponseResult initMessage(Message message);
}
