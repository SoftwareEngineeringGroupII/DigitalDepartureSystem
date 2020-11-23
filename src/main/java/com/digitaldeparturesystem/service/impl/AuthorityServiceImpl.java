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
            return ResponseResult.FAILED("权限名路径");
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
        authoritiesMapper.insertAuthority(authorities);
        sqlSession.close();
        return ResponseResult.SUCCESS("权限增加成功");
    }
}
