package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IProcessService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.service.impl.CommonServiceImpl;
import com.digitaldeparturesystem.service.impl.ProcessServiceImpl;
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
     * @param
     * @return
     */
    @GetMapping("/process")
    public ResponseResult showProcess(){
        return processService.showProcess();
    }


}
