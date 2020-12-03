package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Notice;
import org.apache.ibatis.annotations.Param;

public interface NoticeMapper {

    void save(Notice notice);

    int deleteNotice(String noticeId);
}
