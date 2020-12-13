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
     * 分页查询所有的教务处审核离校情况
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPage")
    public ResponseResult findAllByPage(@RequestParam("start")Integer start,@RequestParam("size") Integer size){
        return eduService.selectAllByPage(start,size);
    }

    @GetMapping("/stuInfoAndProcess/{stuNumber}")
    public ResponseResult testEdu(@PathVariable("stuNumber")String stuNumber){
        return  eduService.findStuDetailForEdu(stuNumber);
    }










}
