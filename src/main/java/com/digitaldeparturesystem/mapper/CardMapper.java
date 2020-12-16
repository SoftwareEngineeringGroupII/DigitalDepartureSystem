package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.CardInfo;
import com.digitaldeparturesystem.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CardMapper {


    //根据学院类型，学生类型，审核状态查询学生信息 -- 一卡通
    List<CardInfo> listStudentCardInfos(@Param("params")Map<String,String> params);

    //根据学号查询一卡通详情(列表)
    Map<String,Object> getStudentByIdForCard(String stuNum);

    //根据学号审核学生一卡通
    int doCheckCard(String stuNumber);

    List<CardInfo> listAllCard();

   // List<CardInfo> findAllByPage;

    //查询某个学生是否存在
    Student findStuByStuNumber(String stuNumber);



}
