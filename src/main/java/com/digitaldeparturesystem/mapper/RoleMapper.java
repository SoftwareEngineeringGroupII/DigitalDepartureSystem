package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Role;

import java.util.List;

public interface RoleMapper {

    void addRole(Role role);

    void deleteRole(String roleId);

    void updateRole(Role role);

    List<Role> getRoles();
}
