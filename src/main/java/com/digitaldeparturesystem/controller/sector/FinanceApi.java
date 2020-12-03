package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.pojo.Finance;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sector/finance")
public class FinanceApi {

    @Autowired
    private IFinanceService financeService;

    /**
     * 根据学生学号查询财务信息
     * @param studentId
     * @return
     */
    @GetMapping("/{studentId}")
    public ResponseResult getFinanceByStudentId(@PathVariable("studentId")String studentId){
        return financeService.getStudentByIdForFinance(studentId);
    }



    /**
     * 按条件分页查询
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param financeStatus
     * @return
     */
    @GetMapping("/selectAllByPageAndType2/{start}/{size}")
    public  ResponseResult selectAllByPageAndType2(@PathVariable("start")Integer start, @PathVariable("size")Integer size,
                                                  @RequestParam String stuDept,@RequestParam String stuType,
                                                  @RequestParam String financeStatus){
        return financeService.findAllByPageAndType(start,size,stuDept,stuType,financeStatus);
    }

    @PutMapping("/{stuId}")
    public ResponseResult doCheckForFinance(@PathVariable("stuId")String stuId){
        return financeService.doCheckForFinance(stuId);
    }

}
