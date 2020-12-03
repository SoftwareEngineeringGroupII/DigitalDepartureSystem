package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface IDormService {

    //上传公告
    ResponseResult uploadNotice(Notice notice, MultipartFile photo);

    //根据学号查询学生后勤处信息
    ResponseResult getStudentByIdForFinance(String studentId);

    //根据学院类型、学生类型、审核状态分页查询
    //ResponseResult findAllByPageAndType(Map<String,Object> map);

    //按条件分页查询
    ResponseResult findAllByPageAndType(Integer start,Integer size,
                                        String stuDept,String stuType,String financeStatus);

    //审核学生后勤处,修改其审核、余额状态
    ResponseResult doCheckForDorm(String stuNumber);
}
