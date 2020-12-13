package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Clerk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

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

    /**
     * 修改clerk
     */
    void updateClerk(Clerk clerk);

    /**
     * 删除用户拥有的角色
     * @param map
     */
    void deleteRoleToUser(Map<String, String> map);

    /**
     * 更新职员密码
     * @param map
     */
    void updatePassword(Map<String, String> map);
}
