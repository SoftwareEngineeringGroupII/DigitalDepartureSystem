package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.service.ISectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/admin/user")
public class UserApi {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ISectorService sectorService;

    /**
     * 注册
     * @return
     */
    @PostMapping("/join_in")
    public ResponseResult register(@RequestBody Clerk clerk,
                                   HttpServletRequest request) {
        return adminService.insertClerk(clerk,request);
    }

    /**
     * 修改用户
     * @param clerk
     * @return
     */
    @PostMapping("/update/{clerkId}")
    public ResponseResult updateClerk(@PathVariable("clerkId")String clerkId,@RequestBody Clerk clerk){
        return adminService.updateClerk(clerkId,clerk);
    }

    /**
     * 删除用户
     * @return
     */
    @PostMapping("/delete/{clerkId}")
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
        return adminService.findClerkById(clerkId);
    }

    @GetMapping("/account/{clerkAccount}")
    public ResponseResult getClerkByAccount(@PathVariable("clerkAccount")String clerkAccount){
        return adminService.getClerkByAccount(clerkAccount);
    }

    /**
     * 获取全部用户
     * @return
     */
    @GetMapping()
    public ResponseResult getAllClerks(){
        return adminService.findAllClerks();
    }


    @PostMapping("/role/{clerkId}")
    public ResponseResult addRoleToUser(@PathVariable("clerkId")String clerkId, @RequestBody List<String> roleIds){
        return adminService.insertRoleToUser(clerkId,roleIds);
    }

    /**
     * 更新用户拥有的角色
     * @param clerkId
     * @return
     */
    @PostMapping("/role/update/{clerkId}")
    public ResponseResult updateRoleToUser(@PathVariable("clerkId")String clerkId, @RequestBody List<String> roleIds){
        return adminService.insertRoleToUser(clerkId,roleIds);
    }

    @PostMapping("/role/delete/{clerkId}")
    public ResponseResult deleteRoleToUser(@PathVariable("clerkId")String clerkId,@RequestBody List<String> roleIds){
        return adminService.deleteRoleToUser(clerkId,roleIds);
    }

    /**
     * 获取用户拥有的角色
     * @return
     */
    @GetMapping("/role/{clerkId}")
    public ResponseResult getRolesByUser(@PathVariable("clerkId")String clerkId){
        return adminService.findRolesByUser(clerkId);
    }

    @GetMapping("/authorities/{clerkId}")
    public ResponseResult getAuthoritiesByUser(@PathVariable("clerkId")String clerkId){
        return sectorService.getAuthoritiesByUser(clerkId);
    }



}
