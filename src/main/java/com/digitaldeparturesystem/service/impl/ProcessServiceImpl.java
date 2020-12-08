package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.ProcessMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
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
    @Autowired
    private IdWorker idWorker;
    private ProcessMapper processMapper;
    private StudentMapper studentMapper;

    /**
     * 显示离校进度审核状况
     * @param stuID
     * @return
     */
    @Override
    public ResponseResult showProcess(String stuID) {
        if (stuID == null){
            return ResponseResult.FAILED("暂无该进程信息");
        }
        String stuStatus = processMapper.showProcess(stuID);
        return ResponseResult.SUCCESS("显示成功！").setData(stuStatus);
    }
    //补充数据

    //仅供测试
    @Override
    public ResponseResult addProcess(Process process) {
        return null;
    }
}
