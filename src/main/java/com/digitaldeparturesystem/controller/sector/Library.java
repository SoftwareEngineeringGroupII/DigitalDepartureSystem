package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Dorm;
import com.digitaldeparturesystem.pojo.Lib;
import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/library")
public class Library {

    /**
     * 通过学生学号查询图书馆详情
     * @param stuNumber
     * @return
     */
    @GetMapping("/stuNumber")
    public ResponseResult getLibraryByStudentId(@PathVariable("stuNumber")String stuNumber){
        return null;
    }

    /**
     * 审核图书管信息
     * @param studentId
     * @param lib
     * @return
     */
    @PostMapping("/studentId")
    public ResponseResult checkLibrary(@PathVariable("studentId")String studentId, @RequestBody Lib lib){
        return null;
    }

    /**
     * 获取全部图书管列表
     * @return
     */
    @GetMapping
    public ResponseResult getAllLibraries(){
        return null;
    }

    /**
     * 获取已审核的图书管列表
     * @return
     */
    @GetMapping("/check")
    public ResponseResult getCheckLibraries(){
        return null;
    }

    /**
     * 获取未审核的图书管列表
     * @return
     */
    @GetMapping("/uncheck")
    public ResponseResult getUnCheckLibraries(){
        return null;
    }

}
