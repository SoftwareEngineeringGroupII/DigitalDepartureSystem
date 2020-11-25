package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Clerk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SectorMapper {

    /**
     * 通过email查找clerk
     * @param email
     * @return
     */
    Clerk findOneByEmail(String email);

    /**
     * 通过账号查找clerk
     * @param clerkAccount
     * @return
     */
    Clerk findOneByClerkAccount(String clerkAccount);

    /**
     * 通过id查找clerk
     * @param clerkId
     * @return
     */
    Clerk findOneById(String clerkId);

    Page<Clerk> findAll(Pageable pageable);

    /**
     * 修改clerk
     */
    void updateClerk(Clerk clerk);
}
