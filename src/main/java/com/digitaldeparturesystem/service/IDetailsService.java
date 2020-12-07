package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;

import java.util.ResourceBundle;

public interface IDetailsService {
    ResponseResult showStuDetails(Student student);
}
