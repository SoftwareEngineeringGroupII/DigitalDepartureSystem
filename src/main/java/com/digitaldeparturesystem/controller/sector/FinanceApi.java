package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.pojo.Finance;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/finance")
public class FinanceApi {

    @Autowired
    private IFinanceService financeService;

    /**
     * zy
     * 根据学生学号查询财务信息
     * @param studentId
     * @return
     */
    @GetMapping("/{studentId}")
    public ResponseResult getFinanceByStudentId(@PathVariable("studentId")String studentId){
        return financeService.getStudentByIdForFinance(studentId);
    }



    /**
     * zy
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


    /**zy
     * 根据学号审核财务情况
     * @param stuNum
     * @return
     */
    @PutMapping("/{stuNum}")
    public ResponseResult doCheckForFinance(@PathVariable("stuNum")String stuNum){
        return financeService.doCheckForFinance(stuNum);
    }


    //--后面数据图统计---//


    /**
     * zy
     *  查询未审核的财务情况
     * @return
     */
    @GetMapping("/noCheck")
    public ResponseResult noCheck(){
        return financeService.noCheck();
    }


    /**
     * zy
     *  查询已审核的财务情况
     * @return
     */
    @GetMapping("/hadCheck")
    public  ResponseResult hadCheck(){
        return financeService.hadCheck();
    }


    /**
     * zy
     *  导出所有财务审核信息
     * @param response
     */
    @GetMapping("/export")
    public void exportFinance(HttpServletResponse response) throws UnsupportedEncodingException {
           financeService.exportAllFinance(response);
    }


    /**
     * zy
     * 查询所有
     * @return
     */
    @GetMapping("/selectAll")
    public ResponseResult selectAll(){
        return financeService.selectAll();
    }


    /**
     * zy
     * 分页查询所有财务信息
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPage")
    public ResponseResult findAllByPage(@RequestParam("start")Integer start,@RequestParam("size") Integer size){
        return financeService.findAllByPage(start,size);
    }




}
