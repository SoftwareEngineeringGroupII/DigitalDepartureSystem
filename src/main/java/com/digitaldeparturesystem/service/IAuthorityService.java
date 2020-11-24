package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IAuthorityService {

    /**
     *
     * @param authorities
     * @return
     */
    ResponseResult addAuthority(Authorities authorities);

    ResponseResult updateAuthority(String authorityId, Authorities authorities);

    ResponseResult deleteAuthority(String authorityId);

    ResponseResult getAllAuthorities();
}
