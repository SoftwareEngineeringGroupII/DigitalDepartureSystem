package com.digitaldeparturesystem.controller.sector;


import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Card;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.MybatisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/card")
public class CardApi {

    @Autowired
    private ISectorService sectorService;

    /**
     * 通过学生id查询一卡通信息
     * @param studentId
     * @return
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseResult getCardByStudentId(@PathVariable("studentId")String studentId){
        return sectorService.getStudentByIdForCard(studentId);
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




    //---11.25增加 一卡通 按条件分页查询：学院、学生类型、审核状态---//

    /**
     *  按学院、学生类型，审核状态
     * @return
     */

    @GetMapping("/selectAllByPage")
    public ResponseResult selectAllByPage(@RequestBody Map map){
        return sectorService.findAllByPageAndType(map);
    }


    /**
     *  获取全部一卡通列表
     * @return
     */
    @GetMapping("/selectAll")
    public  ResponseResult selectAll(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = studentMapper.getStudentList();
      //  System.out.println("studentList---->>>"+studentList);//[]
      /*  if (studentList == null){
            return ResponseResult.FAILED("查询失败");
        }*/
        sqlSession.close();
        return ResponseResult.SUCCESS("查询成功").setData(studentList);
    }


    /**
     * 一卡通审核
     * @return
     */

    @PutMapping("/checkCard/{stuNumber}")
    public ResponseResult  getCheck(@PathVariable("stuNumber") String stuNumber){

        return sectorService.doCheckForCard(stuNumber);
    }



}
