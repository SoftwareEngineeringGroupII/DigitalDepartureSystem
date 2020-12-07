package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Message;

public interface MessageMapper {
    void addMessage(Message message);
    Message findOneById(String messageId);
    void deleteMessage(Message message);
}
