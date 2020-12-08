package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Student;

import java.util.Map;

public interface DetailMapper {
    /**
     * 展示学生信息
     * @param stuId
     */
    Map<String,Object> showDetailsById(String stuId);
}
