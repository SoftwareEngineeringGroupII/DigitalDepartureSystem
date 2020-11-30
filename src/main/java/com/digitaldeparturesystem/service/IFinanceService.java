package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.response.ResponseResult;

import java.util.Map;

public interface IFinanceService {


    //根据学号查询学生财务信息
    ResponseResult getStudentByIdForFinance(String studentId);

    //根据学院类型、学生类型、审核状态分页查询
    ResponseResult findAllByPageAndType(Map<String,Object> map);

    //审核学生一卡通,修改其审核、余额状态
    ResponseResult doCheckForFinance(String stuNumber);


}
