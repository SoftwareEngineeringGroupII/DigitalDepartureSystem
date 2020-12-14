package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.digitaldeparturesystem.utils.TextUtils.isEmpty;

@Service
@Transactional
public class MessageServiceImpl implements IMessageService {
    /**
     * 问题反馈
     */
    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult initMessage(Message message) {
        if (TextUtils.isEmpty(message.getTitle())) {
            return ResponseResult.FAILED("反馈标题不能为空！");
        }
        if (TextUtils.isEmpty(message.getContent())){
            return ResponseResult.FAILED("反馈问题不能为空！");
        }
        if (message.getMessagedate()==null){
            return ResponseResult.FAILED("反馈时间必须设置！");
        }
//        student.setStuId(String.valueOf(idWorker.nextId()));
        //补充数据
        message.setMessageID(String.valueOf(idWorker.nextId()));
        return null;
    }
}
