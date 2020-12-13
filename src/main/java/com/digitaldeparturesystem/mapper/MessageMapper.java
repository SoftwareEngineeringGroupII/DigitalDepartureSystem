package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Message;

public interface MessageMapper {
    Message sendMessage(Message message);
    Message findOneById(String messageId);
    void deleteMessage(Message message);
}
