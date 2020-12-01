package com.digitaldeparturesystem.controller.sector;


import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Card;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICardService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.MybatisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/card")
public class CardApi {


    @Autowired
    private ICardService cardService;

    /**
     * 通过学生学号查询一卡通信息
     * @param studentId 学生学号
     * @return
     */
    @GetMapping("/studentId/{studentNum}")
    public ResponseResult getCardByStudentId(@PathVariable("studentNum")String studentId){
        return cardService.getStudentByIdForCard(studentId);
    }


    /**
     *  条件分页查询---按学院、学生类型，审核状态
     * @return
     */

    @GetMapping("/selectAllByPageAndType/{start}/{size}")
    public ResponseResult selectAllByPageAndType(@PathVariable("start")Integer start, @PathVariable("size")Integer size,
                                                 @RequestParam String stuDept,@RequestParam String stuType,
                                                 @RequestParam String cardStatus){
        return cardService.findAllByPageAndType(start,size,stuDept,stuType,cardStatus);
    }


    @Resource
    private StudentMapper studentMapper;
    /**
     *  获取全部学生信息：仅限于学生表
     * @return
     */
    @GetMapping("/selectAll")
    public  ResponseResult selectAll(){
        List<Student> studentList = studentMapper.getStudentList();

        return ResponseResult.SUCCESS("查询成功").setData(studentList);
    }


    /**
     * 一卡通审核
     * @return
     */

    @PutMapping("/checkCard/{stuId}")
    public ResponseResult  getCheck(@PathVariable("stuId") String stuId){

        return cardService.doCheckForCard(stuId);
    }



    /**
     * 上传公告
     * 识别上传公告人权限,ID,发布类型,
     * @param notice
     * @param photo
     * @return
     */
    @PostMapping("/notice")
    public  ResponseResult uploadNotice(@RequestBody Notice notice, MultipartFile photo) throws IOException {

        return cardService.uploadNotice(notice,photo);
    }


}
