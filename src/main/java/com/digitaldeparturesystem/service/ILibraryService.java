package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface ILibraryService {

    //分页查询所有学生的图书信息
    ResponseResult findAllByPage(Integer start, Integer size);

    //查询某个学生的借书详情
    ResponseResult getStuBookDetail(String stuNumber);

    //审核某个学生某本书归还
    ResponseResult checkLibrary(String stuNumber,String bookId);

    //导出学生图书情况表
    void exportAllLib(HttpServletResponse response) throws UnsupportedEncodingException;

    ResponseResult findAllByPageAndType(Integer start,Integer size,
                                        String stuDept,String stuType,String libStatus);
}
