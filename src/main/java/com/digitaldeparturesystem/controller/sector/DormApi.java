package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Card;
import com.digitaldeparturesystem.pojo.Dorm;
import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sector/dorm")
public class DormApi {

    @GetMapping("/studentId")
    public ResponseResult getDormByStudentId(@PathVariable("studentId")String studentId){
        return null;
    }

    /**
     * 审核一卡通信息
     * @param studentId
     * @param dorm
     * @return
     */
    @PostMapping("/studentId")
    public ResponseResult checkDorm(@PathVariable("studentId")String studentId, @RequestBody Dorm dorm){
        return null;
    }

    /**
     * 获取全部一卡通列表
     * @return
     */
    @GetMapping
    public ResponseResult getAllDorms(){
        return null;
    }

    /**
     * 获取已审核的一卡通列表
     * @return
     */
    @GetMapping("/check")
    public ResponseResult getCheckDorms(){
        return null;
    }

    /**
     * 获取未审核的一卡通列表
     * @return
     */
    @GetMapping("/uncheck")
    public ResponseResult getUnCheckDorms(){
        return null;
    }

}
