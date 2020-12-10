package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IEduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/deu")
public class EduApi {

    @Autowired
    private IEduService eduService;

    /**
     * zy
     * 根据学生id，获取当前学生各部门审核的信息
     * @return
     */
    @GetMapping("/studentId")
    public ResponseResult getCurrentSectionStatus(@PathVariable("studentId")String studentId){
        return null;
    }

    @GetMapping("/check")
    public ResponseResult getCheckEduList(){
        return null;
    }

    @GetMapping("/uncheck")
    public ResponseResult getUnCheckEduList(){
        return null;
    }

    @GetMapping()
    public ResponseResult getAllEduList(){
        return null;
    }

  /*  *//**
     * 分页查询所有的教务处审核离校情况
     * @param start
     * @param size
     * @return
     *//*
    @GetMapping("/findAllByPage")
    public ResponseResult findAllByPage(@RequestParam("start")Integer start,@RequestParam("size") Integer size){
        return eduService.selectAllByPage(start,size);

    }*/


}
