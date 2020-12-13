package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.mapper.MessageMapper;
import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class MessageApi {
    @Resource
    private IMessageService messageService;
    @Resource
    private MessageMapper messageMapper;
    /**
     * 发布信息
     */
    @PostMapping("/message")
    public ResponseResult sendMessage(@RequestBody Message message){
        log.info("message title -->" + message.getTitle());
        log.info("message content -->" + message.getContent());
        log.info("message date -->" + message.getMessagedate());
        return messageService.sendMessage(message);
    }
    /**
     * 删除信息
     */
    @DeleteMapping("/{messageID}")
    public ResponseResult deleteMessage(@PathVariable("messageID") String messageID){
        return messageService.deleteMessage(messageID);
    }
    @PostMapping("/{responseMessageID}")
    public ResponseResult responseMessage(@PathVariable String responseMessageID){
        return messageService.responseMessage(responseMessageID);
    }


}
