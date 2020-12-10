package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/admin/role")
public class RoleApi {

    @Autowired
    private IRoleService roleService;

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping()
    public ResponseResult addRole(@RequestBody Role role){
        return roleService.insertRole(role);
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PostMapping("/update/{roleId}")
    public ResponseResult updateRole(@PathVariable("roleId") String roleId,@RequestBody Role role){
        return roleService.updateRole(roleId,role);
    }

    /**
     * 删除角色
     * @return
     */
    @PostMapping("/delete/{roleId}")
    public ResponseResult deleteRole(@PathVariable String roleId){
        return roleService.deleteRole(roleId);
    }

    /**
     * 获取全部角色
     * @return
     */
    @GetMapping("/getAllRoles")
    public ResponseResult getAllRoles(){
        return roleService.findAllRoles();
    }

    /**
     * 获取某个角色
     */
    @GetMapping("/{roleId}")
    public ResponseResult getRoleById(@PathVariable("roleId") String roleId){
        return roleService.findRoleById(roleId);
    }

    /**
     * 添加权限到角色
     * @return
     */
    @PostMapping("/authority/{roleId}")
    public ResponseResult addAuthorityToRole(@PathVariable("roleId") String roleId, @RequestBody List<String> authorityIds){
        return roleService.insertAuthorityToRole(roleId,authorityIds);
    }

    /**
     * 获取用户拥有的权限
     * @param roleId
     * @return
     */
    @GetMapping("/authority/{roleId}")
    public ResponseResult getAuthorityByRole(@PathVariable("roleId") String roleId){
        return roleService.findAuthorityByRole(roleId);
    }
}
