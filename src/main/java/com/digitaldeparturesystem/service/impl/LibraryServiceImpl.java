package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.LibraryMapper;
import com.digitaldeparturesystem.pojo.LibInfo;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILibraryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service  //图书馆
@Transactional
public class LibraryServiceImpl implements ILibraryService {

     @Resource
     private LibraryMapper libraryMapper;

    /**
     * 分页查询所有
     * @return
     */
    public ResponseResult findAllByPage(Integer start,Integer size){
        if (size == 0){
            size = 5;
        }

        PageHelper.startPage(start,size);
        List<LibInfo> libInfos = libraryMapper.listAllLibrary();
        if (libInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        PageInfo<LibInfo> libInfoPageInfo = new PageInfo<>(libInfos);
        long total = libInfoPageInfo.getTotal();//总记录数
        int pageNum = libInfoPageInfo.getPageNum();//当前页
        int pages = libInfoPageInfo.getPages();//总页数
        int pageSize = libInfoPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",libInfos);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);//总页数
        map.put("pageSize",pageSize);

        return ResponseResult.SUCCESS("查询成功").setData(map);
    }


    /**
     *  分页查询某个学生的借书信息
     * @param stuNumber
     * @return
     */
    public ResponseResult getStuBookDetail(String stuNumber){
        Map<String, Object> map = libraryMapper.bookDetail(stuNumber);
        if (map.isEmpty()) {
           return ResponseResult.FAILED("没有该学生的借书信息");
        }
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return ResponseResult.SUCCESS("查询成功").setData(list);
    }


    /**
     *  根据学号和图书ID确认归还状态
     * @param stuNumber
     * @param bookId
     * @return
     */
    public ResponseResult checkLibrary(String stuNumber,String bookId){
        int i = libraryMapper.checkLibrary(stuNumber, bookId);
        if (i > 0){
            return ResponseResult.SUCCESS("审核成功！");
        }
        return ResponseResult.FAILED("审核失败！请重新操作");
    }










}
