package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Student;
import org.apache.ibatis.annotations.Param;

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

    //根据学院类型，学生类型，审核状态查询学生信息 -- 一卡通
    List<Map<String,Object>> listStudentCardInfos(@Param("params")Map<String,String> params);

    //根据学号查询一卡通详情
    Map<String,Object> getStudentByIdForCard(String stuId);

    //根据学号审核学生一卡通
    int doCheckCard(String stuId);
}
