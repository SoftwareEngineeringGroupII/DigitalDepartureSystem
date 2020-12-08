package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;

import java.util.ResourceBundle;

public interface IDetailsService {
    //获取学生个人信息
    ResponseResult showStuDetailsById(String stuId);
}
