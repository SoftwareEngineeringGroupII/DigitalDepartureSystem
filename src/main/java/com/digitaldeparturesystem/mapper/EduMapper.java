package com.digitaldeparturesystem.mapper;

import java.util.List;
import java.util.Map;

public interface EduMapper {


    List<Map<String,Object>> listAllEdu();

    //设置学生的离校进度表--一卡通状态
    void setCardStatus(String stuNumber);

    //设置学生的离校进度表--财务状态
    void setFinanceStatus(String stuNumber);

    //设置学生的离校进度表--图书馆状态
    void setLibStatus(String stuNumber);

    //设置学生的离校进度表--后勤状态
    void setDormStatus(String stuNumber);

    Map<String,Object> findProcess(String stuNumber);




}
