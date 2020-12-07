package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.service.IProcessService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.digitaldeparturesystem.utils.TextUtils.isEmpty;

@Service
@Transactional
public class ProcessServiceImpl implements IProcessService {
    /**
     * 显示离校进度审核状况
     * @param processid
     * @return
     */
    @Override
    public ResponseResult showProcess(String processid) {
        return null;
    }

    @Override
    public ResponseResult addProcess(Process process) {
        return null;
    }
}
