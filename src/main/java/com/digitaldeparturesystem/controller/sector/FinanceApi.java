package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Finance;
import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sector/finance")
public class FinanceApi {

    /**
     * 根据学生id查询财务信息
     * @param studentId
     * @return
     */
    @GetMapping("/{studentId}")
    public ResponseResult getFinanceByStudentId(@PathVariable("studentId")String studentId){
        return null;
    }

    /**
     * 审核学生财务
     * @param studentId
     * @param finance
     * @return
     */
    @PostMapping("/{studentId}")
    public ResponseResult checkFinance(@PathVariable("studentId")String studentId,@RequestBody Finance finance){
        return null;
    }

    /**
     * 获取已审核的学生财务清单
     * @return
     */
    @GetMapping("/check")
    public ResponseResult getCheckFinances(){
        return null;
    }

    /**
     * 获取未审核的学生财务清单
     * @return
     */
    @GetMapping("/uncheck")
    public ResponseResult getUnCheckFinances(){
        return null;
    }

}
