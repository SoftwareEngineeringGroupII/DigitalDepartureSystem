package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.pojo.Form;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFormService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class FormApi {
    @Resource
    private IFormService formService;
    /**
     * 表单显示
     */
    @GetMapping("/formStudent")
    public ResponseResult showFormByStudent(){
        return formService.showFormByStudent();
    }
    @GetMapping("/formProcess")
    public ResponseResult showFormByProcess(){
        return formService.showFormByProcess();
    }
}
