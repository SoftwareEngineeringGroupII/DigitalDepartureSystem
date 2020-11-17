package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Refreshtoken;

public interface RefreshTokenMapper {

    int deleteAllByUserId(String id);

    void save(Refreshtoken refreshtoken);

    Refreshtoken findOneByTokenKey(String tokenKey);

}
