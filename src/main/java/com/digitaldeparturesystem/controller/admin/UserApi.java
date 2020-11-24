package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/user")
public class UserApi {

    @Autowired
    private IAdminService adminService;

    /**
     * 添加权限
     * @param clerk
     * @return
     */
    @PostMapping()
    public ResponseResult addClerk(@RequestBody Clerk clerk){
        return adminService.addClerk(clerk);
    }

    /**
     * 修改权限
     * @param clerk
     * @return
     */
    @PutMapping("/{clerkId}")
    public ResponseResult updateClerk(@PathVariable("clerkId")String clerkId,@RequestBody Clerk clerk){
        return adminService.updateClerk(clerkId,clerk);
    }

    /**
     * 删除权限
     * @return
     */
    @DeleteMapping("/{clerkId}")
    public ResponseResult deleteClerk(@PathVariable("clerkId")String clerkId){
        return adminService.deleteClerk(clerkId);
    }

    /**
     * 查找单个用户
     * @param clerkId
     * @return
     */
    @GetMapping("/{clerkId}")
    public ResponseResult getClerkById(@PathVariable("clerkId")String clerkId){
        return null;
    }

    /**
     * 获取全部用户
     * @return
     */
    @GetMapping()
    public ResponseResult getAllClerks(){
        return null;
    }

}
