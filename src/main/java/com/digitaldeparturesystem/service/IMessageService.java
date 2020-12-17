package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IMessageService {
    /**
     * 发送信息
     * @param message
     * @return
     */
    ResponseResult sendMessage(Message message);

    /**
     * 删除信息
     * @param messageID
     * @return
     */
    ResponseResult deleteMessage(String messageID);


    /**
     * 显示已读信息
     * @return
     */
    ResponseResult showMessageRead();
    /**
     * 显示未读信息
     */
    ResponseResult showMessageUnRead();
}
