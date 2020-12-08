package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IProcessService {
    //获取进程信息
    ResponseResult showProcess(String stuID);
    //添加进程
    ResponseResult addProcess(Process process);
}
