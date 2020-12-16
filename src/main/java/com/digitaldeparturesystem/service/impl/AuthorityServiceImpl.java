package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.AuthoritiesMapper;
import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAuthorityService;
import com.digitaldeparturesystem.utils.AuthorityTreeUtils;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    private IdWorker idWorker;

    @Resource
    private AuthoritiesMapper authoritiesMapper;

    @Override
    public ResponseResult insertAuthority(Authorities authorities) {
        //检查数据
        String authoritiesName = authorities.getName();
        if (TextUtils.isEmpty(authoritiesName)){
            return ResponseResult.FAILED("权限名不能为空");
        }
        if (TextUtils.isEmpty(authorities.getUrl())){
            return ResponseResult.FAILED("权限名路径不能为空");
        }
        if (TextUtils.isEmpty(authorities.getResourceType())){
            return ResponseResult.FAILED("权限类型不能为空");
        }
        if (authorities.getIndex() == 0){
            return ResponseResult.FAILED("权限序列不能为空");
        }
        Authorities authorityFromDB = authoritiesMapper.findByName(authorities.getName());
        if (authorityFromDB != null){
            return ResponseResult.FAILED("权限名已存在");
        }
        //补充数据
        authorities.setId(String.valueOf(idWorker.nextId()));
        authorities.setAvailable(1);
        //插入
        authoritiesMapper.insertAuthority(authorities);
        return ResponseResult.SUCCESS("权限增加成功");
    }

    @Override
    public ResponseResult updateAuthority(String authorityId, Authorities authorities) {
        //从数据库获取数据
        Authorities authorityFromDB = authoritiesMapper.getAuthorityById(authorityId);
        if (authorityFromDB == null){
            return ResponseResult.FAILED("该权限不存在");
        }
        //检查数据
        if (!TextUtils.isEmpty(authorities.getName())){
            authorityFromDB.setName(authorities.getName());
        }
        if (!TextUtils.isEmpty(authorities.getUrl())){
            authorityFromDB.setUrl(authorities.getUrl());
        }
        //更新
        authoritiesMapper.updateAuthority(authorityFromDB);
        return ResponseResult.SUCCESS("更新权限成功");
    }

    @Override
    public ResponseResult deleteAuthority(String authorityId) {
        //查找数据是否存在
        Authorities authorityById = authoritiesMapper.getAuthorityById(authorityId);
        if (authorityById == null){
            return ResponseResult.FAILED("该权限不存在");
        }
        //把一级权限下的二级权限删完
        List<Authorities> children = authoritiesMapper.findChildrenByParentId(authorityId);
        for (Authorities child : children) {
            authoritiesMapper.deleteAuthorities(child.getId());
        }
        //删除一级权限
        authoritiesMapper.deleteAuthorities(authorityId);
        return ResponseResult.SUCCESS("删除权限成功");
    }

    @Resource
    private RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public ResponseResult findAllAuthorities() {
        //得到一级菜单
        List<Authorities> allAuthorities = authoritiesMapper.findByParentIsNullOrderByIndex();
        for (Authorities authority : allAuthorities) {
            //得到子菜单
            AuthorityTreeUtils.getChildrenToMenu(roleAuthorityMapper,authority);
        }
        return ResponseResult.SUCCESS("获取全部权限列表成功").setData(allAuthorities);
    }

    @Override
    public ResponseResult findAuthorityById(String authorityId) {
        //拿到数据
        Authorities authority = authoritiesMapper.getAuthorityById(authorityId);
        if (authority == null){
            return ResponseResult.FAILED("此权限不存在");
        }
        //查询子数据
        AuthorityTreeUtils.getChildrenToMenu(roleAuthorityMapper,authority);
        return ResponseResult.SUCCESS("查找权限成功").setData(new ArrayList<Authorities>(){{add(authority);}});
    }
}
