package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserRoleMapper {

    /**
     * 给用户添加角色
     *
     */
    void addRoleToUser(Map<String,String> map);

    /**
     * 删除该用户的所有角色
     * @param clerkId
     */
    void deleteAllRoleByUser(String clerkId);

    /**
     * 获取用户所拥有的权限
     * @param clerkId
     */
    List<Role> getRolesByUser(String clerkId);

    /**
     * 查询数据是否已经存在
     * @param map
     * @return
     */
    String findUserRoleData(Map<String,String> map);
}
