package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IProcessService {
    /**
     * 获取进程信息
     * @return
     */
    ResponseResult showProcess();

    /**
     * 添加进程
     * @param process
     * @return
     */
    ResponseResult addProcess(Process process);
}
