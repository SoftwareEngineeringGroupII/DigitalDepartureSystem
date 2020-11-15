package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Clerk;
import org.apache.ibatis.annotations.Insert;

public interface AdminMapper {

    //保存职员信息
    int addClerk(Clerk clerk);

}
