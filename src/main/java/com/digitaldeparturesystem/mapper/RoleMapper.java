package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Role;

import java.util.List;

public interface RoleMapper {

    /**
     * 添加角色
     * @param role
     */
    void addRole(Role role);

    /**
     * 删除角色
     * @param roleId
     */
    void deleteRole(String roleId);

    /**
     * 更新角色
     * @param role
     */
    void updateRole(Role role);

    /**
     * 得到全部角色
     * @return
     */
    List<Role> getRoles();

    /**
     * 通过角色id查找角色
     * @param roleId
     * @return
     */
    Role getRoleById(String roleId);

    /**
     * 通过roleName查找Role
     * @param roleName
     * @return
     */
    Role getRoleByName(String roleName);
}
