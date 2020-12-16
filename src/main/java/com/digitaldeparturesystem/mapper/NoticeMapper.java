package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {

    void save(Notice notice);

    void deleteNotice(String noticeID);

    List<Notice> findSelfNotice(String ClerkId);

    void setNoticePass(String noticeID);

    void setNoticeRefuse(String noticeID);

    List<Notice> viewAllByPeople();

    Notice viewNoticeDetails(String noticeID);

    List<Notice> NoCheckNotice();

    List<Notice> haveCheckNotice();

    List<Notice> RefuseNotice();

    List<Notice> draftNotice();



}
