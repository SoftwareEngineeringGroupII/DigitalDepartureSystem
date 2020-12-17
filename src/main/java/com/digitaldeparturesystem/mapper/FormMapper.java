package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Form;

public interface FormMapper {
    /**
     * 展示学生离校表单
     * @param stuID
     * @return
     */
    Form showFormByStudent(String stuID);
    Form showFormByProcess(String stuID);

    /**
     * 打印表单
     */
    void printForm();
}
