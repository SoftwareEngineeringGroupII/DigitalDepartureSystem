package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IProcessService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class ProcessApi {
    @Autowired
    private IProcessService processService;
    /**
     * 进程审核状态
     */
    @PostMapping("/student_process")
    public ResponseResult showProcess(@RequestBody Process process){
        return null;
    }
}
