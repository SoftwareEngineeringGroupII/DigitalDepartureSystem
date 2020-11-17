package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Clerk;

public interface SectorMapper {

    Clerk findOneByEmail(String email);

    Clerk findOneByClerkAccount(String clerkAccount);

    Clerk findOneById(String clerkId);

}
