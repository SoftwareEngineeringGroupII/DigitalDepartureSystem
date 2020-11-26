package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IAuthorityService {

    /**
     * 添加权限
     * @param authorities
     * @return
     */
    ResponseResult insertAuthority(Authorities authorities);

    /**
     * 更新权限
     * @param authorityId
     * @param authorities
     * @return
     */
    ResponseResult updateAuthority(String authorityId, Authorities authorities);

    /**
     * 删除权限
     * @param authorityId
     * @return
     */
    ResponseResult deleteAuthority(String authorityId);

    /**
     * 获取所有得权限
     * @return
     */
    ResponseResult findAllAuthorities();

    /**
     * 通过权限id查找权限
     * @param authorityId
     * @return
     */
    ResponseResult findAuthorityById(String authorityId);
}
