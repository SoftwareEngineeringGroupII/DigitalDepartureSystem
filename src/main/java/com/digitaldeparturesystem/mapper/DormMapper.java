package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.DormInfo;
import com.digitaldeparturesystem.response.ResponseResult;

import java.util.List;
import java.util.Map;

public interface DormMapper {

    //按条件分页查询退寝状态
    List<Map<String,Object>> listStudentDormInfos(Map<String,String> params);

    //审核学生退寝情况
    void doCheckForDorm(String stuNum);

    //根据学号获取学生退寝情况
    DormInfo getStudentByIdForDorm(String stuNumber);

    //导出所有学生退寝情况
    List<DormInfo> listAllDorm();

}
