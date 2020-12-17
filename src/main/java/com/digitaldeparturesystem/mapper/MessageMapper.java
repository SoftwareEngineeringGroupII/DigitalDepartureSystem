package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;

import java.util.List;

public interface MessageMapper {
    /**
     * 发送信息
     * @param message
     * @return
     */
    void sendMessage(Message message);

    /**
     * 查找消息
     * @param messageID
     * @return
     */
    Message findOneById(String messageID);

    /**
     * 显示已读信息
     * @param
     * @return
     */
    List<Message> showMessageRead();
    /**
     * 显示未读信息
     */
    List<Message> showMessageUnRead();
    /**
     * 删除信息
     * @param messageID
     */
    void deleteMessage(String messageID);

    /**
     * 保存信息
     * @param
     */
    String getIdByName(String name);
}
