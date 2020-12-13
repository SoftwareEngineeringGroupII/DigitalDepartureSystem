package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.StuBasicInfo;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.pojo.Process;

import java.util.List;
import java.util.Map;

public interface EduMapper {


    //查询所有离校状态
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

    //查询某个学生是否存在
    Student findStuByStuNumber(String stuNumber);

    //根据学号查询离校状态(列表)
    Map<String,Object> findStuEduByStuNumber(String stuNumber);

    //根据学号查询离校审核详情
   StuBasicInfo getStuBasicInfo(String stuNumber);

   Process getStuProcess(String stuNumber);





}
