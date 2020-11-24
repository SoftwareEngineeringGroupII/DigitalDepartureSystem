package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IRoleService {

    ResponseResult addRole(Role role);

    ResponseResult updateRole(String roleId, Role role);

    ResponseResult deleteRole(String roleId);

    ResponseResult getAllRoles();

}
