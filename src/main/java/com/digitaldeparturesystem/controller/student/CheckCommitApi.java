package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.mapper.CheckcommitMapper;
import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICheckcommitService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class CheckCommitApi {
    @Resource
    private ICheckcommitService checkcommitService;

    /**
     * 提交审核
     */
    @PostMapping("/commit")
    public ResponseResult addCheckCommit(@RequestBody Checkcommit checkcommit){
        return checkcommitService.addCheckCommit(checkcommit);
    }
    /**
     * 显示提交的审核
     */
    @GetMapping("/showCommit")
    public ResponseResult showCheckCommit(){
        return checkcommitService.showCheckCommit();
    }
}
