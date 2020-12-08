package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.response.ResponseResult;

public interface ILibraryService {

    //分页查询所有学生的图书信息
    ResponseResult findAllByPage(Integer start, Integer size);

    //查询某个学生的借书详情
    ResponseResult getStuBookDetail(String stuNumber);

    //审核某个学生某本书归还
    ResponseResult checkLibrary(String stuNumber,String bookId);
}
