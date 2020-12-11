package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Checkcommit;

public interface CheckcommitMapper {
    //新增审核
    Checkcommit addCheckCommitByType(Checkcommit checkcommit);
    //保存审核
    Checkcommit saveCheckcommit(Checkcommit checkcommit);
    //修改审核
    Checkcommit updateCheckcommit(Checkcommit checkcommit);
}
