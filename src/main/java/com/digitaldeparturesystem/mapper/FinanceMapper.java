package com.digitaldeparturesystem.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FinanceMapper {

    //根据学生学号查询财务缴费情况
    Map<String,Object> getStudentByIdForFinance(String stuNumber);

    //按条件分页查询学生财务缴费情况
    List<Map<String,Object>> listStudentFinanceInfos(@Param("params")Map<String,String> params);

    void doCheckForFinance(String stuID);
}
