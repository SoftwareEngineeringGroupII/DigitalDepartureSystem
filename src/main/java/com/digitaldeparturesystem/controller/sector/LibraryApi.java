package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.mapper.LibraryMapper;
import com.digitaldeparturesystem.pojo.Dorm;
import com.digitaldeparturesystem.pojo.Lib;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/library")
public class LibraryApi {

    @Autowired
    private ILibraryService libraryService;


    /**
     * zy
     * 分页查询所有学生的结束信息
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPage")
    public ResponseResult findAllByPage(@RequestParam("start")Integer start,@RequestParam("size") Integer size){
        return libraryService.findAllByPage(start,size);
    }


    /**
     * zy
     * 查询某个学生借书详情
     * @param stuNumber
     * @return
     */
    @GetMapping("/getStuBookDetail/{stuNumber}")
    public ResponseResult getStuBookDetail(@PathVariable("stuNumber") String stuNumber){
        //判断传进来的学号是否为空
        if (stuNumber == null) {
            return ResponseResult.FAILED("请重新输入信息");
        }
       return libraryService.getStuBookDetail(stuNumber);
    }


    /**
     *  zy
     *  审核学生某本书接口
     * @param stuNumber
     * @return
     */
    @PutMapping("/checkLibrary/{stuNumber}/{bookId}")
    public ResponseResult checkLibrary(@PathVariable("stuNumber")String stuNumber,
                                       @PathVariable("bookId") String bookId){

       return libraryService.checkLibrary(stuNumber,bookId);

    }


    /**
     * zy
     *  导出所有财务审核信息
     * @param response
     */
    @GetMapping("/export")
    public void exportFinance(HttpServletResponse response) throws UnsupportedEncodingException {
        libraryService.exportAllLib(response);
    }



}
