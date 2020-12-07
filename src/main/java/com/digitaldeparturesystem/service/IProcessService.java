package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IProcessService {
    ResponseResult showProcess(String processid);
    ResponseResult addProcess(Process process);
}
