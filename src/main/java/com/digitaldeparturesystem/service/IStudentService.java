package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IStudentService extends UserDetailsService {

    /**
     * 插入学生数据
     * @param student
     * @return
     */
    ResponseResult insertStudent(Student student);

    /**
     * 检查学生登录
     * @return
     */
    Student checkStudent();

    /**
     * 创建学生Token
     * @param httpServletResponse
     * @param student
     */
    String createToken(HttpServletResponse httpServletResponse, Student student);
}
