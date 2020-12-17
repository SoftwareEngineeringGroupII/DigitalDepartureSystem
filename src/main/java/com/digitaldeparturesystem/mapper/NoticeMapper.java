package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {

    void save(Notice notice);

    void deleteNotice(String noticeID);

    List<Notice> findSelfNotice(String ClerkId);

    List<Notice> findSelfNotice2(String ClerkId);

    List<Notice> findSelfNotice1(String ClerkId);

    List<Notice> findSelfNotice3(String ClerkId);

    List<Notice> findSelfNotice0(String ClerkId);

    void setNoticePass(String noticeID);

    void setNoticeRefuse(String noticeID);

    List<Notice> viewAllByPeople();

    Notice viewNoticeDetails(String noticeID);

    List<Notice> NoCheckNotice();

    List<Notice> haveCheckNotice();

    List<Notice> RefuseNotice();

    List<Notice> draftNotice();

    void setTop(String noticeID);

    void setUnTop(String noticeID);

    List<Notice> viewTopNotice();

    List<Notice> viewUnTopNotice();

    List<Notice>  searchNotice(String department);

    List<Notice> searchNotice0(String apartment);

    List<Notice> searchNotice2(String apartment);

    List<Notice> searchNotice1(String apartment);



}
