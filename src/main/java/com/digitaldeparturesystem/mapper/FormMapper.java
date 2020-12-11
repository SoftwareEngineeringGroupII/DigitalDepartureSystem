package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Form;

public interface FormMapper {
    //展示离校表单信息
    Form showFormByStudent(String stuID);
    Form showFormByProcess(String stuID);
    //打印表单
    //void printForm();
}
