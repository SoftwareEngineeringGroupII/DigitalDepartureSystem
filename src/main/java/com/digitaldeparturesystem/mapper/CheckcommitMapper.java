package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Checkcommit;
import com.digitaldeparturesystem.pojo.Message;

import java.util.List;

public interface CheckcommitMapper {
    /**
     * 提交审核
     * @param checkcommit
     * @return
     */
    void addCheckCommit(Checkcommit checkcommit);

    /**
     * 显示已提交审核
     * @return
     */
    List<Checkcommit> showCheckCommit();

    /**
     * 修改审核
     * @param checkcommit
     * @return
     */
    void updateCheckcommit(Checkcommit checkcommit);
}
