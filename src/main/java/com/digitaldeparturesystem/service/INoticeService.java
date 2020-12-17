package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

public interface INoticeService {
    ResponseResult uploadNotice(HttpServletRequest request, Notice notice);

    ResponseResult findSelfNotice(HttpServletRequest request,Integer start,Integer size);

    ResponseResult viewByAllPeople() throws ParseException;

    ResponseResult viewNoticeDetail(String noticeID);

    ResponseResult checkNotice(String noticeID,String result);

    ResponseResult findAllNotice(Integer start,Integer size);

    ResponseResult deleteNotice(String noticeID);

    ResponseResult setTop(String noticeID);

    ResponseResult setUnTop(String noticeID);

    ResponseResult uploadNoticeBySuper(HttpServletRequest request,Notice notice);

    ResponseResult continueNotice(String noticeID);

    ResponseResult draftNotice(HttpServletRequest request,Notice notice);

    ResponseResult searchNotice(String department,Integer start,Integer size);
}
