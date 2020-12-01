package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface IStudentService {

    ResponseResult findManagerAccount(Student student, HttpServletRequest request);

}
