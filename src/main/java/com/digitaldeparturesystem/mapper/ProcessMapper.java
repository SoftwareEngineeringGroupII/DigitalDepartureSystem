package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Process;

public interface ProcessMapper {
    //获取进程审核状态
    String showProcess(String stuID);
    //添加进程信息
    void addProcess(Process process);
}
