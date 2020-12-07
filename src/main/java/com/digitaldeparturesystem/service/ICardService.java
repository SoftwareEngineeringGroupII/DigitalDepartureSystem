package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface ICardService {



    //上传公告
    ResponseResult uploadNotice(Notice notice, MultipartFile photo) throws IOException;

    //获取全部学生信息
   // public ResponseResult listStuAll(int page, int size, HttpServletRequest request, HttpServletResponse response);



    //查询所有学生
  //  ResponseResult findAllByPage(Map<String,Object> map);

    //根据学院类型、学生类型、审核状态分页查询
  //  ResponseResult findAllByPageAndType(Map<String,Object> map);

    ResponseResult findAllByPageAndType(Integer start,Integer size,String stuDept,String stuType,String cardStatus);

    //根据学号查询学生一卡通详情
    ResponseResult getStudentByIdForCard(String studentId);

    //审核学生一卡通,修改其审核、余额状态
    ResponseResult doCheckForCard(String stuNumber);

    //导出所有学生一卡通信息
    void exportAllCard(HttpServletResponse response) throws UnsupportedEncodingException;

}


