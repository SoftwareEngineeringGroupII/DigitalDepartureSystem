package com.digitaldeparturesystem.controller.sector;


import com.digitaldeparturesystem.pojo.Card;
import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/card")
public class CardApi {

    /**
     * 通过学生id查询一卡通信息
     * @param studentId
     * @return
     */
    @GetMapping("/studentId")
    public ResponseResult getCardByStudentId(@PathVariable("studentId")String studentId){
        return null;
    }

    /**
     * 审核一卡通信息
     * @param studentId
     * @param card
     * @return
     */
    @PostMapping("/studentId")
    public ResponseResult checkCard(@PathVariable("studentId")String studentId, @RequestBody Card card){
        return null;
    }

    /**
     * 获取全部一卡通列表
     * @return
     */
    @GetMapping
    public ResponseResult getAllCards(){
        return null;
    }

    /**
     * 获取已审核的一卡通列表
     * @return
     */
    @GetMapping("/check")
    public ResponseResult getCheckCards(){
        return null;
    }

    /**
     * 获取未审核的一卡通列表
     * @return
     */
    @GetMapping("/uncheck")
    public ResponseResult getUnCheckCards(){
        return null;
    }

}
