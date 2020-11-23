package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Authorities;

import java.util.List;

public interface AuthoritiesMapper {

    List<Authorities> getRolePermissions(String userId);

    List<Authorities> findByParentIsNullOrderByIndex();

    List<Authorities> findChildrenByParentId(String parentId);

    Authorities findByName(String name);

    void insertAuthority(Authorities authorities);
}
