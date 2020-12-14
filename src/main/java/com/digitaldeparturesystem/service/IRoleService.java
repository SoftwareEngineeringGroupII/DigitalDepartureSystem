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
    ResponseResult insertRole(Role role);

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
    ResponseResult findAllRoles();

    /**
     * 通过id获取role
     * @return
     */
    ResponseResult findRoleById(String roleId);

    /**
     * 为角色添加权限
     * @param roleId
     * @return
     */
    ResponseResult insertAuthorityToRole(String roleId, List<String> authorityIds);

    /**
     * 通过角色id获取角色所拥有的权限
     * @param roleId
     * @return
     */
    ResponseResult findAuthorityByRole(String roleId);

    /**
     * 添加单个权限给role
     * @param roleId
     * @param authorityId
     * @return
     */
    ResponseResult insertSingleAuthorityToUser(String roleId, String authorityId);
}
