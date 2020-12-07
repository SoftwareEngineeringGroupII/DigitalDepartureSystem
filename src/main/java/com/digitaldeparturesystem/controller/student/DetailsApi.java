package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDetailsService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class DetailsApi {
    @Autowired
    private IDetailsService detailsService;

    /**
     * 显示学生个人信息
     */
    @GetMapping("/student_details")
    public ResponseResult showDetails(@RequestBody Student student) {
        return detailsService.showStuDetails(student);
    }
}
