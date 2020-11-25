package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.mapper.RoleMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IRoleService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.MybatisUtils;
import com.digitaldeparturesystem.utils.TextUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult addRole(Role role) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        //检查数据
        if (TextUtils.isEmpty(role.getCode())){
            return ResponseResult.FAILED("角色代码不能为空");
        }
        if (TextUtils.isEmpty(role.getName())){
            return ResponseResult.FAILED("角色名字不能为空");
        }
        if (role.getIndex() == 0){
            return ResponseResult.FAILED("角色序号不能为空");
        }
        //补充数据
        role.setId(String.valueOf(idWorker.nextId()));
        //添加
        roleMapper.addRole(role);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("添加角色成功");
    }

    @Override
    public ResponseResult updateRole(String roleId, Role role) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        //数据库种获取数据
        Role roleFromDB = roleMapper.getRoleById(roleId);
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
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("更新角色成功");
    }

    @Override
    public ResponseResult deleteRole(String roleId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        if (checkRoleIsExist(roleId, roleMapper)) {
            return ResponseResult.FAILED("角色不存在");
        }
        roleMapper.deleteRole(roleId);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("删除角色成功");
    }

    @Override
    public ResponseResult getAllRoles() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        List<Role> roles = roleMapper.getRoles();
        sqlSession.close();
        return ResponseResult.SUCCESS("获取全部角色成功").setData(roles);
    }

    @Override
    public ResponseResult getRoleById(String roleId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        Role role = roleMapper.getRoleById(roleId);
        sqlSession.close();
        return ResponseResult.SUCCESS("获取角色成功").setData(role);
    }

    @Override
    public ResponseResult addAuthorityToRole(String roleId, List<Authorities> authoritiesList) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleAuthorityMapper roleAuthorityMapper = sqlSession.getMapper(RoleAuthorityMapper.class);
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        //检查数据
        if (checkRoleIsExist(roleId, roleMapper))
            return ResponseResult.FAILED("角色不存在");
        //添加数据
        for (Authorities authorities : authoritiesList) {
            roleAuthorityMapper.addAuthorityToRole(String.valueOf(idWorker.nextId()),roleId,authorities.getId());
        }
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("向角色添加权限成功");
    }

    @Override
    public ResponseResult getAuthorityByRole(String roleId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RoleAuthorityMapper roleAuthorityMapper = sqlSession.getMapper(RoleAuthorityMapper.class);
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        //检查数据
        if (checkRoleIsExist(roleId, roleMapper))
            return ResponseResult.FAILED("角色不存在");
        List<Authorities> authorities = roleAuthorityMapper.getAuthorityByRole(roleId);
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
