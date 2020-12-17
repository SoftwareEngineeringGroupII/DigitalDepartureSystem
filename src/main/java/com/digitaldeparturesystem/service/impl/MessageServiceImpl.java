package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.MessageMapper;
import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service("IMessageService")
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private MessageMapper messageMapper;
    /**
     * 问题反馈
     */
    @Autowired
    private IdWorker idWorker;

    /**
     * 提交申请
     * @param message
     * @return
     */
    @Override
    public ResponseResult sendMessage(Message message) {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);

        //assert student != null;


        if (TextUtils.isEmpty(message.getContent())){
            return ResponseResult.FAILED("申请内容不能为空！");
        }
        message.setMessagedate(new Date());
        message.setMsgStatus("0");
        message.setMessageID(idWorker.nextId()+"");
        message.setSendID(student.getStuId());

        //保存数据
        messageMapper.sendMessage(message);
        return ResponseResult.SUCCESS("提交申请成功");
    }


    /**
     * 删除信息
     * @param messageID
     * @return
     */
    @Override
    public ResponseResult deleteMessage(String messageID) {
        messageMapper.deleteMessage(messageID);
        return ResponseResult.SUCCESS("删除成功！");
    }

    /**
     * 已读显示
     * @return
     */

    @Override
    public ResponseResult showMessageRead() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        List<Message> messages = messageMapper.showMessageRead();
        if (messages.isEmpty()){
            return ResponseResult.FAILED("暂无消息显示");
        }
        return ResponseResult.SUCCESS("显示成功！").setData(messages);
    }

    @Override
    public ResponseResult showMessageUnRead() {
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);
        assert student != null;
        List<Message> messages = messageMapper.showMessageUnRead();
        if (messages.isEmpty()){
            return ResponseResult.FAILED("暂无消息显示");
        }
        return ResponseResult.SUCCESS("显示成功！").setData(messages);
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
