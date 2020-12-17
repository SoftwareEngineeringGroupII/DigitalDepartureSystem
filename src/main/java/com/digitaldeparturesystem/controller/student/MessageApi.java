package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.mapper.MessageMapper;
import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@CrossOrigin
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
    @PostMapping("/sendMessage")
    public ResponseResult sendMessage(@RequestBody Message message){
        log.info("message title -->" + message.getTitle());
        log.info("message content -->" + message.getContent());
        log.info("message date -->" + message.getMessagedate());
        return messageService.sendMessage(message);
    }
    /**
     * 删除信息
     * @return
     */
    @PostMapping("/deletemessage")
    public ResponseResult deleteMessage(String messageID){
        return messageService.deleteMessage(messageID);
    }

    /**
     * 重新发送审核
     */
    @PostMapping("/ReSendMessage")
    public ResponseResult ResendMessage(@RequestBody Message message){
        return messageService.reSendMessage(message);
    }
    /**
     * 获取已读信息数据
     * @return
     */
    @GetMapping("/showmessageRead")
    public ResponseResult showMessageRead(){
        return messageService.showMessageRead();
    }
    /**
     * 获取未读信息数据
     */
    @GetMapping("/showmessageUnread")
    public ResponseResult showMessageUnRead(){
        return messageService.showMessageUnRead();
    }
}
