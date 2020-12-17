package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface IEduService {

    ResponseResult selectAllByPage(Integer start, Integer size);

    ResponseResult getStudentByIdForEdu(String stuNumber);

    ResponseResult findStuDetailForEdu(String stuNumber);

    void exportAllStuBasicInfo(HttpServletResponse response) throws UnsupportedEncodingException;

    ResponseResult viewMessage(String stuNumber);

    ResponseResult doCheckForEduRefuse(String stuNumber, Message message);

    ResponseResult doCheckForEduPass(String stuNumber);

    ResponseResult findAllByPageAndType(Integer start, Integer size,
                                        String stuDept, String stuType, String isLeave);

   // ResponseResult testSelectAll();


}
