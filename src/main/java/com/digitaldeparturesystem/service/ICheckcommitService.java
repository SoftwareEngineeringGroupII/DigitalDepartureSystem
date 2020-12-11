package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.response.ResponseResult;

public interface ICheckcommitService {
    ResponseResult addCheckCommitByType(Checkcommit checkcommit);
}
