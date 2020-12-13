package com.digitaldeparturesystem.controller.sector;


import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Card;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
@RestController
@RequestMapping("/sector/card")
public class CardApi {


    @Autowired
    private ICardService cardService;

    /**
     * zy
     * 通过学生学号查询一卡通信息
     * @param studentId 学生学号
     * @return
     */
    @GetMapping("/studentId/{studentNum}")
    public ResponseResult getCardByStudentId(@PathVariable("studentNum")String studentId){
        return cardService.getStudentByIdForCard(studentId);
    }


    /**
     * zy
     *  条件分页查询---按学院、学生类型，审核状态
     * @return
     */

    @GetMapping("/selectAllByPageAndType/{start}/{size}")
    public ResponseResult selectAllByPageAndType(@PathVariable("start")Integer start, @PathVariable("size")Integer size,
                                                 @RequestParam String stuDept,@RequestParam String stuType,
                                                 @RequestParam String cardStatus){
        return cardService.findAllByPageAndType(start,size,stuDept,stuType,cardStatus);
    }


    /**
     * zy
     * 一卡通审核
     * @return
     */

    @PostMapping("/checkCard/{stuNumber}")
    public ResponseResult  getCheck(@PathVariable("stuNumber") String stuNumber){

        return cardService.doCheckForCard(stuNumber);
    }





    /**
     * zy
     * 上传公告
     * 识别上传公告人权限,ID,发布类型,
     * @param notice
     * @param photo
     * @return
     */
    @PostMapping("/notice")
    public  ResponseResult uploadNotice(@RequestBody Notice notice, MultipartFile photo,
                                        HttpServletRequest request,HttpServletResponse response) throws IOException {

        return cardService.uploadNotice(notice,photo,request,response);
    }



    /**
     * zy
     * 导出所有学生一卡通审核信息
     * @param response
     * @return
     */
    @GetMapping("/export")
    public ResponseResult exportCard(HttpServletResponse response) {
        try {
            cardService.exportAllCard(response);
        }catch (Exception e){
            return ResponseResult.FAILED("导出失败");
        }
        return ResponseResult.SUCCESS("导出成功");
    }



    /**
     * zy
     * 查询所有一卡通信息
     * @return
     */
    @GetMapping("/selectAll")
    public ResponseResult selectAll(){
        return cardService.selectAll();
    }



    /**
     * zy
     * 分页查询所有一卡通信息
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPage")
    public  ResponseResult findAllByPage(@RequestParam("start")Integer start,@RequestParam("size")Integer size){
        return cardService.findAllByPage(start,size);
    }







}
