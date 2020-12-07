package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICheckcommitService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class CheckServiceImpl implements ICheckcommitService {
    @Autowired
    private IdWorker idWorker;
    @Override
    public ResponseResult checkcommit(Checkcommit checkcommit) {
        //检查审核提交是否合法
        if (TextUtils.isEmpty(checkcommit.getStuId())){
            return ResponseResult.FAILED("学号不能为空!");
        }
        if (TextUtils.isEmpty(checkcommit.getReason())){
            return ResponseResult.FAILED("审核理由不能为空!");
        }
        if (TextUtils.isEmpty(checkcommit.getType())){
            return ResponseResult.FAILED("审核类型不能为空!");
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        //补充数据
        checkcommit.setCheckId(String.valueOf(idWorker.nextId()));
        //获取提交审核时间
        checkcommit.setCommitdate(date);
        return null;
    }
}
