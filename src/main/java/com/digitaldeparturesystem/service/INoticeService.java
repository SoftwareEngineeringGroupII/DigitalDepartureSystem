package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface INoticeService {
    ResponseResult uploadNotice(HttpServletRequest request, Notice notice);

    ResponseResult findSelfNotice(HttpServletRequest request,Integer start,Integer size);

    ResponseResult viewByAllPeople();

    ResponseResult viewNoticeDetail(String noticeID);

    ResponseResult checkNotice(String noticeID,String result);

    ResponseResult findAllNotice(Integer start,Integer size);

    ResponseResult deleteNotice(String noticeID);
}
