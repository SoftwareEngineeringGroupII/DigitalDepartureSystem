package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.DormInfo;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface IFinanceService {

    //上传公告
    ResponseResult uploadNotice(Notice notice, MultipartFile photo);

    //根据学号查询学生财务信息
    ResponseResult getStudentByIdForFinance(String studentId);

    //根据学院类型、学生类型、审核状态分页查询
    //ResponseResult findAllByPageAndType(Map<String,Object> map);

    ResponseResult findAllByPageAndType(Integer start, Integer size,
                                        String stuDept, String stuType, String financeStatus);

    //审核学生一卡通,修改其审核、余额状态
    ResponseResult doCheckForFinance(String stuNumber);

    //查询已经审核的财务情况
    ResponseResult hadCheck();

    //查询未审核的财务情况
    ResponseResult noCheck();

    //导出财务所有信息
    void exportAllFinance(HttpServletResponse response) throws UnsupportedEncodingException;

    //查询所有学生财务缴费情况
    ResponseResult selectAll();

    //分页查询所有
    ResponseResult findAllByPage(Integer start,Integer size);



}