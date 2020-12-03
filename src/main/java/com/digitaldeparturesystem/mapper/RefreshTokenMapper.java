package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Refreshtoken;

public interface RefreshTokenMapper {

    int deleteAllByUserId(String clerkId);

    void insertRefreshToken(Refreshtoken refreshtoken);

    Refreshtoken findOneByTokenKey(String tokenKey);

}
