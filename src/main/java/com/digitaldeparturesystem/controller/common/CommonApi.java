package com.digitaldeparturesystem.controller.common;

import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICommonService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonApi {

    @Autowired
    private ICommonService commonService;

    @GetMapping("/menu")
    public ResponseResult getMenu(){
        return commonService.getMenu();
    }

}
