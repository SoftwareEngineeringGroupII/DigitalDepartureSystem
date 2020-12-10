package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Clerk;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface AdminMapper {

    /**
     * 添加Clerk
     * @param clerk
     * @return
     */
    int addClerk(Clerk clerk);

    /**
     * 查找所有的clerk
     * @return
     */
    List<Clerk> findAllClerks();


    /**
     * 删除clerk
     * @param clerkId
     */
    void deleteClerk(String clerkId);


}
