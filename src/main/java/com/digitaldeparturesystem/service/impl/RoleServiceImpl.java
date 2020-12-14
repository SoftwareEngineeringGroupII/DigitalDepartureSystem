package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.mapper.RoleMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IRoleService;
import com.digitaldeparturesystem.utils.AuthorityTreeUtils;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IdWorker idWorker;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public ResponseResult insertRole(Role role) {

        //检查数据
        if (TextUtils.isEmpty(role.getCode())){
            return ResponseResult.FAILED("角色代码不能为空");
        }
        String roleName = role.getName();
        Role roleFromDB = roleMapper.getRoleByName(roleName);
        if (roleFromDB != null){
            return ResponseResult.FAILED("角色名已存在");
        }
        if (TextUtils.isEmpty(roleName)){
            return ResponseResult.FAILED("角色名字不能为空");
        }
        if (role.getIndex() == 0){
            return ResponseResult.FAILED("角色序号不能为空");
        }
        //补充数据
        role.setId(String.valueOf(idWorker.nextId()));
        //添加
        roleMapper.addRole(role);
        return ResponseResult.SUCCESS("添加角色成功");
    }

    @Override
    public ResponseResult updateRole(String roleId, Role role) {
        //数据库种获取数据
        Role roleFromDB = roleMapper.getRoleById(roleId);
        if (checkRoleIsExist(roleId,roleMapper)){
            return ResponseResult.FAILED("该角色不存在");
        }
        //更新数据
        if (!TextUtils.isEmpty(role.getCode())) {
            roleFromDB.setCode(role.getCode());
        }
        if (!TextUtils.isEmpty(role.getName())) {
            roleFromDB.setName(role.getName());
        }
        roleFromDB.setIndex(role.getIndex());
        //更新
        roleMapper.updateRole(roleFromDB);
        return ResponseResult.SUCCESS("更新角色成功");
    }

    @Override
    public ResponseResult deleteRole(String roleId) {
        if (checkRoleIsExist(roleId, roleMapper)) {
            return ResponseResult.FAILED("角色不存在");
        }
        roleMapper.deleteRole(roleId);
        return ResponseResult.SUCCESS("删除角色成功");
    }

    @Override
    public ResponseResult findAllRoles() {
        List<Role> roles = roleMapper.getRoles();
        return ResponseResult.SUCCESS("获取全部角色成功").setData(roles);
    }

    @Override
    public ResponseResult findRoleById(String roleId) {
        Role role = roleMapper.getRoleById(roleId);
        if (checkRoleIsExist(roleId,roleMapper)){
            return ResponseResult.FAILED("该角色不存在");
        }
        List<Object> list = new ArrayList<>();
        list.add(role);
        return ResponseResult.SUCCESS("获取角色成功").setData(list);
    }

    @Override
    public ResponseResult insertAuthorityToRole(String roleId, List<String> authorityIds) {
        //检查数据
        if (checkRoleIsExist(roleId, roleMapper))
            return ResponseResult.FAILED("角色不存在");
        //添加数据
        Map<String,String> map = new HashMap<>();
        for (String  authorityId: authorityIds) {
            map.put("id",String.valueOf(idWorker.nextId()));
            map.put("roleId",roleId);
            map.put("authorityId",authorityId);
            roleAuthorityMapper.addAuthorityToRole(map);
        }
        return ResponseResult.SUCCESS("向角色添加权限成功");
    }

    @Override
    public ResponseResult findAuthorityByRole(String roleId) {
        //检查数据
        if (checkRoleIsExist(roleId, roleMapper))
            return ResponseResult.FAILED("角色不存在");
        List<Authorities> authorities = roleAuthorityMapper.getAuthorityNoParentByRole(roleId);
        for (Authorities authority : authorities) {
            AuthorityTreeUtils.getChildrenToMenu(roleAuthorityMapper,authority);
        }
        return ResponseResult.SUCCESS("获取角色的权限成功").setData(authorities);
    }


    private boolean checkRoleIsExist(String roleId, RoleMapper roleMapper) {
        //先从数据库中获取数据
        Role roleById = roleMapper.getRoleById(roleId);
        if (roleById == null) {
            return true;
        }
        return false;
    }
}
