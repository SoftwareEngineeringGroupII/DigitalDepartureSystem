package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Role;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper {

    /**
     * 给用户添加角色
     * @param clerkId
     */
    void addRoleToUser(String id,String clerkId,String roleId);

    /**
     * 删除该用户的所有角色
     * @param clerkId
     */
    void deleteAllRoleByUser(String clerkId);

    /**
     * 获取用户所拥有的权限
     * @param clerkId
     */
    void getRolesByUser(String clerkId);
}
