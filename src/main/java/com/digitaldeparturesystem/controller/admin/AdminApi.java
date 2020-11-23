package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminApi {

    @Autowired
    private IAdminService adminService;

    /**
     * 初始化管理员账号
     * @param clerk
     * @return
     */
    @PostMapping("/admin_account")
    public ResponseResult initAdminAccount(@RequestBody Clerk clerk){
        log.info("admin name --> " + clerk.getClerkName());
        log.info("admin pwd --> " + clerk.getClerkPwd());
        log.info("admin account --> " + clerk.getClerkAccount());
        return adminService.initManagerAccount(clerk);
    }

    @GetMapping("/menu")
    public ResponseResult getAllMenu(){
        return adminService.findAuditMenu();
    }


}
