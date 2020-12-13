package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.response.ResponseResult;

public interface IEduService {

    ResponseResult selectAllByPage(Integer start, Integer size);

    ResponseResult findStuDetailForEdu(String stuNumber);
}
