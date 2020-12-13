package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Authorities;

import java.util.List;
import java.util.Map;

public interface RoleAuthorityMapper {

    /**
     * 向role中添加权限
     *
     */
    void addAuthorityToRole(Map<String,String> map);

    /**
     * 通过roleid，获取authorities
     * @param roleId
     * @return
     */
    List<Authorities> getAuthorityNoParentByRole(String roleId);

    /**
     * 找到子菜单
     * @return
     */
    List<Authorities> findChildrenByParentId(String parentId);

    /**
     * 删除角色所拥有的权限
     * @param roleId
     */
    void deleteAllAuthorityByRole(String roleId);
}
