package com.digitaldeparturesystem.controller.student;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentApi {

    @Autowired
    private IStudentService studentService;


    /**
     * 注册
     * @param student
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody Student student){
        return studentService.insertStudent(student);
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
