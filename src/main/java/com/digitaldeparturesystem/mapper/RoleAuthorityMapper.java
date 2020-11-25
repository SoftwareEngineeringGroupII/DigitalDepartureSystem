package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Authorities;

import java.util.List;

public interface RoleAuthorityMapper {

    /**
     * 向role中添加权限
     * @param
     * @param id
     */
    void addAuthorityToRole(String id, String roleId, String authoritiesId);

    /**
     * 通过roleid，获取authorities
     * @param roleId
     * @return
     */
    List<Authorities> getAuthorityByRole(String roleId);
}
