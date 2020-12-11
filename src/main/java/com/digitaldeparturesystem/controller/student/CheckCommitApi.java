package com.digitaldeparturesystem.controller.student;
import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICheckcommitService;
import com.digitaldeparturesystem.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class CheckCommitApi {

    private ICheckcommitService checkcommitService;
    /**
     * 提交审核
     */
    @PostMapping("/checkcommit")
    //添加审核
    public ResponseResult addCheckCommitByType(@RequestBody Checkcommit checkcommit){
        return checkcommitService.addCheckCommitByType(checkcommit);
    }
    //修改审核
    public ResponseResult updateCheckcommit(@RequestBody Checkcommit checkcommit){return null;}
}
