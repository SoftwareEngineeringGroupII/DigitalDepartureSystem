package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Card;
import com.digitaldeparturesystem.pojo.Dorm;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.print.PrinterGraphics;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/dorm")
public class DormApi {

    @Autowired
    private IDormService dormService;


    /**
     * zy
     *  根据学号获取学生退寝详情
     * @param stuNumber
     * @return
     */
    @GetMapping("/stuNumber/{stuNumber}")
    public ResponseResult getDormByStudentId(@PathVariable("stuNumber")String stuNumber){
        return dormService.getStudentByIdForFinance(stuNumber);
    }



    /**
     * zy
     * 审核一卡通信息
     * @param stuNumber
     * @return
     */
    @PutMapping("/stuNumber/{stuNumber}")
    public ResponseResult checkDorm(@PathVariable("stuNumber")String stuNumber){
        return dormService.doCheckForDorm(stuNumber);
    }




    /**
     * zy
     * 按条件分页查询寝室审核情况
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param isLeave
     * @return
     */
    @GetMapping("/selectAllByPageAndType/{start}/{size}")
    public ResponseResult selectAllByPageAndType(@PathVariable("start")Integer start, @PathVariable("size")Integer size,
                                                 @RequestParam String stuDept,@RequestParam String stuType,
                                                 @RequestParam String isLeave){
        return dormService.findAllByPageAndType(start,size,stuDept,stuType,isLeave);
    }




    /**
     * zy
     * 导出寝室审核表
     * @param response
     * @return
     */
    @GetMapping("/export")
    public ResponseResult exportDorm(HttpServletResponse response){
        try {
            dormService.exportAllDorm(response);
        }catch (Exception e){
            return ResponseResult.FAILED("导出失败");
        }
        return ResponseResult.SUCCESS("导出成功");
    }

    /**
     * zy
     *  查询所有退寝状态
     * @return
     */
    @GetMapping("/selectAll")
    public ResponseResult selectAll(){
        return dormService.selectAll();
    }


    /**
     *  zy
     *  分页查询所有的数据
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPage")
    public ResponseResult selectAllByPage(@RequestParam("start")Integer start,@RequestParam("size")Integer size){
        return dormService.selectAllByPage(start,size);
    }




}
