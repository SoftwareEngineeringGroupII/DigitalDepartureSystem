package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Clerk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SectorMapper {

    Clerk findOneByEmail(String email);

    Clerk findOneByClerkAccount(String clerkAccount);

    Clerk findOneById(String clerkId);

    Page<Clerk> findAll(Pageable pageable);
}
