package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.response.ResponseResult;

public interface ICheckcommitService {
    //提交审核
    ResponseResult addCheckCommit(Checkcommit checkcommit);
}
