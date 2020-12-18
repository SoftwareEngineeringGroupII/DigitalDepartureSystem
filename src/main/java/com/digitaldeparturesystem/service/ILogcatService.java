package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.LogSearchCondition;
import com.digitaldeparturesystem.response.ResponseResult;

public interface ILogcatService {

    /**
     * 获取用户的日志
     * @param account
     * @param condition
     * @return
     */
    ResponseResult getLogs(String account, LogSearchCondition condition);
}
