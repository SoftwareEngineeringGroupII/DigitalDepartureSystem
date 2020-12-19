package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.MessageMapper;
import com.digitaldeparturesystem.mapper.RoleMapper;
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
    @Resource
    private RoleMapper roleMapper;

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
        message.setReceiveID("教务处");

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

    @Override
    public ResponseResult reSendMessage(Message message) {
        String title = message.getTitle();//获取标题
        String content = message.getContent();//获取内容
        HttpServletRequest request = getRequest();
        //用户cookies里面获取token
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //解析*
        Student student = TokenUtils.parseStudentByTokenKey(redisUtils, tokenKey);


        //根据学生id查sendid
        Message messageFromDB = null;
        messageFromDB = messageMapper.findMessageBySendId(student.getStuNumber());
        if (messageFromDB == null){
            //sendid没查到，查revId
            messageFromDB = messageMapper.findMessageByRecvId(student.getStuName());
            if (messageFromDB == null){
                //无消息，可插入
                if (TextUtils.isEmpty(message.getContent())){
                    return ResponseResult.FAILED("申请内容不能为空！");
                }
                message.setMessagedate(new Date());
                message.setMsgStatus("0");
                message.setMessageID(idWorker.nextId()+"");
                message.setSendID(student.getStuNumber());
                message.setReceiveID("教务处");
                //保存数据
                messageMapper.sendMessage(message);
                return ResponseResult.SUCCESS("提交申请成功");
            }else{
                //有消息，可覆盖
                if (TextUtils.isEmpty(title)) {
                    return ResponseResult.FAILED("反馈标题不能为空");
                }
                if (TextUtils.isEmpty(content)) {
                    return ResponseResult.FAILED("反馈内容不能为空");
                }
                messageMapper.setMessage(messageFromDB.getMessageID(),title,content);
            }
        }else{
            //有消息，可覆盖
            if (TextUtils.isEmpty(title)) {
                return ResponseResult.FAILED("反馈标题不能为空");
            }
            if (TextUtils.isEmpty(content)) {
                return ResponseResult.FAILED("反馈内容不能为空");
            }
            messageMapper.setMessage(messageFromDB.getMessageID(),title,content);
        }
        return ResponseResult.SUCCESS("重新提交成功");
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
