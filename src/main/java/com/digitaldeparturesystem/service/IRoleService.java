package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;

import java.util.List;

public interface IRoleService {

    /**
     * 添加角色
     * @param role
     * @return
     */
    ResponseResult addRole(Role role);

    /**
     * 更新角色
     * @param roleId
     * @param role
     * @return
     */
    ResponseResult updateRole(String roleId, Role role);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    ResponseResult deleteRole(String roleId);

    /**
     * 获取全部角色
     * @return
     */
    ResponseResult getAllRoles();

    /**
     * 通过id获取role
     * @return
     */
    ResponseResult getRoleById(String roleId);

    /**
     * 为角色添加权限
     * @param roleId
     * @param authoritiesList
     * @return
     */
    ResponseResult addAuthorityToRole(String roleId, List<Authorities> authoritiesList);

    /**
     * 通过角色id获取角色所拥有的权限
     * @param roleId
     * @return
     */
    ResponseResult getAuthorityByRole(String roleId);
}
