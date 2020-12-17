package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.response.ResponseResult;

public interface ICheckcommitService {
    /**
     * 提交审核
     * @param checkcommit
     * @return
     */
    ResponseResult addCheckCommit(Checkcommit checkcommit);

    /**
     * 显示提交的审核
     * @param
     * @return
     */
    ResponseResult showCheckCommit();
}
