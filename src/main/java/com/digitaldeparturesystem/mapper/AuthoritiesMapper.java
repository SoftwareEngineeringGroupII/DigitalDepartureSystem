package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Authorities;

import java.util.List;

public interface AuthoritiesMapper {

    /**
     * 通过用户的id，得到用户拥有的权限
     * @param userId
     * @return
     */
    List<Authorities> getRolePermissions(String userId);

    /**
     * 找到所有的一级权限，当parent_id不为null时为一级权限
     * @return
     */
    List<Authorities> findByParentIsNullOrderByIndex();

    /**
     * 通过一级权限，找二级权限
     * @param parentId
     * @return
     */
    List<Authorities> findChildrenByParentId(String parentId);

    /**
     * 插入权限
     * @param authorities
     */
    void insertAuthority(Authorities authorities);

    /**
     * 获取全部权限
     * @return
     */
    List<Authorities> getAllAuthorities();

    /**
     * 通过id获取权限
     * @param authorityId
     * @return
     */
    Authorities getAuthorityById(String authorityId);

    /**
     * 更新权限
     * @param authority
     */
    void updateAuthority(Authorities authority);

    /**
     * 删除权限
     * @param authorityId
     */
    void deleteAuthorities(String authorityId);

    /**
     * 通过姓名查找权限
     * @param authorityName
     * @return
     */
    Authorities getAuthorityByName(String authorityName);
}
