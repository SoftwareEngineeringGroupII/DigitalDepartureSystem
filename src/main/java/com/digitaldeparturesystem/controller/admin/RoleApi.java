package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Authorities;
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
        return roleService.addRole(role);
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/{roleId}")
    public ResponseResult updateClerk(@PathVariable("roleId") String roleId,@RequestBody Role role){
        return roleService.updateRole(roleId,role);
    }

    /**
     * 删除角色
     * @return
     */
    @DeleteMapping("/{roleId}")
    public ResponseResult deleteClerk(@PathVariable String roleId){
        return roleService.deleteRole(roleId);
    }

    /**
     * 获取全部角色
     * @return
     */
    @GetMapping("/getAllRoles")
    public ResponseResult getAllRoles(){
        return roleService.getAllRoles();
    }

    /**
     * 获取某个角色
     */
    @GetMapping("/{roleId}")
    public ResponseResult getRoleById(@PathVariable("roleId") String roleId){
        return roleService.getRoleById(roleId);
    }

    /**
     * 添加权限到角色
     * @return
     */
    @PostMapping("/authority/{roleId}")
    public ResponseResult addAuthorityToRole(@PathVariable("roleId") String roleId, List<Authorities> authoritiesList){
        return roleService.addAuthorityToRole(roleId,authoritiesList);
    }

    @GetMapping("/authority/{roleId}")
    public ResponseResult getAuthorityByRole(@PathVariable("roleId") String roleId){
        return roleService.getAuthorityByRole(roleId);
    }
}
