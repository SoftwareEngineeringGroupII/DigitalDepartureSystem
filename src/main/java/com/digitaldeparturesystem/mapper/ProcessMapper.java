package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Process;

public interface ProcessMapper {
    /**
     * 获取进程审核状态
     * @param stuID
     * @return
     */
    Process showProcess(String stuID);

    /**
     * 添加进程信息
     * @param process
     */
    void addProcess(Process process);
}
