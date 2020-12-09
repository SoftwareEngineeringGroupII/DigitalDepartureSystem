package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.LibInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LibraryMapper {


    //查询所有图书借书信息
    List<LibInfo> listAllLibrary();

    Map<String,Object> bookDetail(String stuNumber);

    int checkLibrary(@Param("stuNumber") String stuNumber, @Param("bookId") String bookId);

}
