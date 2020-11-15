package com.digitaldeparturesystem.controller.student;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentApi {

    @Autowired
    private IStudentService studentService;

    /**digital_departure_system
     * 初始化学生账号 -init-admin
     * @return
     */
    @PostMapping("/student_account")
    public ResponseResult initManagerAccount(@RequestBody Student student, HttpServletRequest request){
        log.info("student name --> " + student.getStuName());
        log.info("student password --> " + student.getStuPwd());
        return studentService.initManagerAccount(student,request);
    }

    /**
     * 注册
     * @param student
     * @return
     */
    @PostMapping
    public ResponseResult register(@RequestBody Student student){
        return null;
    }

    /**
     *
     * @param captcha
     * @param student
     * @return
     */
    @PostMapping("/{captcha}")
    public ResponseResult login(@PathVariable("captcha") String captcha,@RequestBody Student student){
        return null;
    }

    /**
     * 获取图灵验证码
     * @return
     */
    @GetMapping("/captcha")
    public ResponseResult getCaptcha(){
        return null;
    }


    /**
     * 修改密码
     * @param student
     * @return
     */
    @PostMapping("/password")
    public ResponseResult updatePassword(@RequestBody Student student){
        return null;
    }


    /**
     * 获取用户信息
     * @param stuId
     * @return
     */
    @GetMapping("/{stuId}")
    public ResponseResult getStudentInfo(@PathVariable("stuId")String stuId){
        return null;
    }

    /**
     * 修改用户信息user-info
     * @return
     */
    @PutMapping
    public ResponseResult updateStuInfo(@RequestBody Student student){
        return null;
    }
}
