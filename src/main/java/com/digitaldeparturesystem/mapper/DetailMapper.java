package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Student;

public interface DetailMapper {
    /**
     * 展示学生信息
     * @param
     */
    Student showDetails(String stuID);
}
