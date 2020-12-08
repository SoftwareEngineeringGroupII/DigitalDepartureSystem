package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.LibInfo;

import java.util.List;
import java.util.Map;

public interface LibraryMapper {


    //查询所有图书借书信息
    List<LibInfo> listAllLibrary();

    Map<String,Object> bookDetail();

}
