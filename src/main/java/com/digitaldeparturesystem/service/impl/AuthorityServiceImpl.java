package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.AuthoritiesMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAuthorityService;
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
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult addAuthority(Authorities authorities) {
        //检查数据
        if (TextUtils.isEmpty(authorities.getName())){
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
        //补充数据
        authorities.setId(String.valueOf(idWorker.nextId()));
        authorities.setAvailable(1);
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        //插入
        authoritiesMapper.insertAuthority(authorities);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("权限增加成功");
    }

    @Override
    public ResponseResult updateAuthority(String authorityId, Authorities authorities) {
        //从数据库获取数据
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        Authorities authorityFromDB = authoritiesMapper.getAuthorityById(authorityId);
        //检查数据
        if (!TextUtils.isEmpty(authorities.getName())){
            authorityFromDB.setName(authorities.getName());
        }
        if (!TextUtils.isEmpty(authorities.getUrl())){
            authorityFromDB.setUrl(authorities.getUrl());
        }
        //更新
        authoritiesMapper.updateAuthority(authorityFromDB);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("更新权限成功");
    }

    @Override
    public ResponseResult deleteAuthority(String authorityId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        //查找数据是否存在
        Authorities authorityById = authoritiesMapper.getAuthorityById(authorityId);
        if (authorityById == null){
            return ResponseResult.FAILED("该权限不存在");
        }
        //删除
        authoritiesMapper.deleteAuthorities(authorityId);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("删除权限成功");
    }

    @Override
    public ResponseResult getAllAuthorities() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        List<Authorities> allAuthorities = authoritiesMapper.getAllAuthorities();
        sqlSession.close();
        return ResponseResult.SUCCESS("获取全部权限列表成功").setData(allAuthorities);
    }

    @Override
    public ResponseResult getAuthorityById(String authorityId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        Authorities authority = authoritiesMapper.getAuthorityById(authorityId);
        sqlSession.close();
        return ResponseResult.SUCCESS("查找权限成功").setData(authority);
    }
}
