package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Student;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

    //获取学生列表
    List<Student> getStudentList();

    //根据学生id查询学生
    Student getStudentById(String id);

    //插入一个学生
    int addStudent(Student student);

    //修改学生信息
    int updateStudent(Student student);

    //修改学生信息
    int updateStudent2(Map<String,Object> student);

    //删除学生信息
    int deleteStudent(String id);
}
