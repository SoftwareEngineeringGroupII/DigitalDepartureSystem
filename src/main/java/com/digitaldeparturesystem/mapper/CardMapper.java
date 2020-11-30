package com.digitaldeparturesystem.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CardMapper {


    //根据学院类型，学生类型，审核状态查询学生信息 -- 一卡通
    List<Map<String,Object>> listStudentCardInfos(@Param("params")Map<String,String> params);

    //根据学号查询一卡通详情
    Map<String,Object> getStudentByIdForCard(String stuId);

    //根据学号审核学生一卡通
    int doCheckCard(String stuId);
}
