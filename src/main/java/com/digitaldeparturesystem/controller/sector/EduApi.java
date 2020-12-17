package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.pojo.Message;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IEduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/edu")
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

    /**
     * 按条件分页查询
     * @param stuDept
     * @param stuType
     * @param checkStatus
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPageAndType/{start}/{size}")
    public ResponseResult findAllByPageAndType(@RequestParam String stuDept,
                                               @RequestParam String stuType,
                                               @RequestParam String checkStatus,
                                               @PathVariable("start") Integer start,
                                               @PathVariable("size") Integer size){
           return eduService.findAllByPageAndType(start,size,stuDept,stuType,checkStatus);
    }

    /**
     * zy
     *  根据学号查询某学生(列表显示)
     * @param stuNumber
     * @return
     */
    @GetMapping("/studentId/{stuNumber}")
    public ResponseResult getStudentByIdForEdu(@PathVariable("stuNumber")String stuNumber){
          return eduService.getStudentByIdForEdu(stuNumber);
    }



    /**
     * zy
     * 某学生详情 --学号依据
     * @param stuNumber
     * @return
     */
    @GetMapping("/stuInfoAndProcess/{stuNumber}")
    public ResponseResult stuInfoAndProcess(@PathVariable("stuNumber")String stuNumber){
        return  eduService.findStuDetailForEdu(stuNumber);
    }


    /**
     * zy
     *  导出所有学生基本信息
     * @param response
     */
    @GetMapping("/export")
    public void exportFinance(HttpServletResponse response) throws UnsupportedEncodingException {
        eduService.exportAllStuBasicInfo(response);
    }






    /**
     * zy
     * 查看某学生发送的申请消息
     * @param stuNumber
     * @return
     */
    @GetMapping("/message/{stuNumber}")
    public ResponseResult viewMessage(@PathVariable("stuNumber")String stuNumber){
        return  eduService.viewMessage(stuNumber);
    }




    /**
     * zy
     * 教务处审核 -- 拒绝(有表单回复)
     * @param stuNumber
     * @param message
     * @return
     */
    @PostMapping("/checkRefuse/{stuNumber}")
    public ResponseResult EduCheckRefuse(@PathVariable("stuNumber")String stuNumber, @RequestBody Message message){
        return eduService.doCheckForEduRefuse(stuNumber,message);
    }




    /**
     * zy
     * 教务处审核 -- 通过(无表单回复)
     * @param stuNumber
     * @return
     */
    @PostMapping("/checkPass/{stuNumber}")
    public ResponseResult EduCheckPass(@PathVariable("stuNumber")String stuNumber){
        return eduService.doCheckForEduPass(stuNumber);
    }


   /* @GetMapping("/testSelectAll")
    public ResponseResult testSelectAll(){
        return eduService.testSelectAll();
    }
*/








}
