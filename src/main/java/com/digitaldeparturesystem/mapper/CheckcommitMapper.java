package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Checkcommit;

public interface CheckcommitMapper {
    //新增审核
    int addCheckcommit(Checkcommit checkcommit);
    //保存审核
    void saveCheckcommit(Checkcommit checkcommit);
    //修改审核
    int updateCheckcommit(Checkcommit checkcommit);
}
