package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
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
        return adminService.insertManagerAccount(clerk);
    }

    /**
     * 审核公告
     * @param articleId
     * @param notice
     * @return
     */
    @PostMapping("/checkNotice/{noticeId}")
    public ResponseResult checkNotice(@PathVariable("noticeId")String articleId,@RequestBody Notice notice){
        return null;
    }


}
