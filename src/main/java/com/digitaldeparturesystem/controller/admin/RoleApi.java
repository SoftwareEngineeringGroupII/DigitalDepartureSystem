package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/role")
public class RoleApi {


    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping()
    public ResponseResult addClerk(@RequestBody Role role){
        return null;
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/{roleId}")
    public ResponseResult updateClerk(@PathVariable("roleId") String roleId,@RequestBody Role role){
        return null;
    }

    /**
     * 删除角色
     * @return
     */
    @DeleteMapping("/{roleId}")
    public ResponseResult deleteClerk(@PathVariable String roleId){
        return null;
    }

    /**
     * 获取全部角色
     * @return
     */
    @GetMapping("/getAllRoles")
    public ResponseResult getAllRoles(){
        return null;
    }

    /**
     * 获取某个角色
     */
    @GetMapping("/{roleId}")
    public ResponseResult getRoleById(@PathVariable("roleId") String roleId){
        return null;
    }

}
