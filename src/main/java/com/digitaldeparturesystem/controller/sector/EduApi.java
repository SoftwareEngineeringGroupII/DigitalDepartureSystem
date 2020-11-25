package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/deu")
public class EduApi {

    /**
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

}
