package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICardService;
import com.digitaldeparturesystem.service.INoticeService;
import com.digitaldeparturesystem.service.ISectorService;
import com.google.gson.internal.$Gson$Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/notice")
public class NoticeApi {

    @Autowired
    private INoticeService noticeService;

  /*  *//**
     * 公共页面显示
     * @return
     *//*
    @GetMapping("/viewByAllPeople")
    public ResponseResult viewAllByPeople() throws Exception{
        return noticeService.viewByAllPeople();
    }
*/

    /**
     *  查看公告详情
     * @param noticeID
     * @return
     */
    @GetMapping("/viewNoticeDetails/{noticeID}")
    public ResponseResult viewNoticeDetails(@PathVariable("noticeID") String noticeID){
        return noticeService.viewNoticeDetail(noticeID);
    }


    /**
     * 上传notice
     * @param notice
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult uploadNotice(HttpServletRequest request, @RequestBody Notice notice){
        return noticeService.uploadNotice(request,notice);
    }

    /**
     * 分页查询自己的公告
     * @param request
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findSelfNotice/{start}/{size}")
    public ResponseResult findSelfNotice(HttpServletRequest request,
                                             @PathVariable("start")Integer start,
                                             @PathVariable("size")Integer size){
        return noticeService.findSelfNotice(request,start,size);
    }

    /**
     * 保存至草稿箱
     * @param request
     * @param notice
     * @return
     */
    @PostMapping("/draftNotice")
    public ResponseResult draftNotice(HttpServletRequest request,Notice notice){
        return noticeService.draftNotice(request,notice);
    }

    /**
     * 继续编辑公告
     * @param noticeID
     * @return
     */
    @GetMapping("/continueNotice")
    public ResponseResult continueNotice(String noticeID){
        return noticeService.continueNotice(noticeID);
    }

    /**
     * 删除公告
     * @param noticeID
     * @return
     */
    @GetMapping("/deleteNotice/{noticeID}")
    public ResponseResult deleteNotice(@PathVariable("noticeID") String noticeID){
        return  noticeService.deleteNotice(noticeID);
    }




}
