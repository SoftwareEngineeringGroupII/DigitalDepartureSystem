package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Finance;
import com.digitaldeparturesystem.pojo.FinanceInfo;
import com.digitaldeparturesystem.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FinanceMapper {

    //根据学生学号查询财务缴费情况
    FinanceInfo getStudentByIdForFinance(String stuNumber);

    //按条件分页查询学生财务缴费情况
    //List<Map<String,Object>> listStudentFinanceInfos(@Param("params")Map<String,String> params);
    List<FinanceInfo> listStudentFinanceInfos(@Param("params")Map<String,String> params);

    //根据学号审核财务状态
    void doCheckForFinance(String stuNum);


    //查询已经审核的财务情况
    List<FinanceInfo> listHadCheck();

    //查询未审核的财务情况
    //List<Map<String,Object>> listNoCheck();
    List<FinanceInfo> listNoCheck();

    List<FinanceInfo> listAllFinance();

    //查询某个学生是否存在
    Student findStuByStuNumber(String stuNumber);

    int findCardStatus(String stuNumber);

    int findDormStatus(String stuNumber);

    int findLibStatus(String stuNumber);

    void sumExpense(String stuNumber);



}
