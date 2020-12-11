package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class MessageApi {
    @Autowired
    private IMessageService messageService;
    /**
     * 发布反馈
     */
    @PostMapping("/addmessage")
    public ResponseResult initMessage(@RequestBody Message message){
        log.info("message title -->" + message.getTitle());
        log.info("message content -->" + message.getContent());
        log.info("message date -->" + message.getMessagedate());
        return messageService.initMessage(message);
    }
    /**
     * 删除反馈
     */
    @DeleteMapping("/{messageID}")
    public ResponseResult deleteMessage(@PathVariable("messageID") String messageID){
        return null;
    }


}
