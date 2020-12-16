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

    /**
     * 公共页面显示
     * @return
     */
    @GetMapping("/viewByAllPeople")
    public ResponseResult viewAllByPeople(){
        return noticeService.viewByAllPeople();
    }

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
     * 测试上传notice
     * @param notice
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult uploadNotice(HttpServletRequest request, @RequestBody Notice notice){
        return noticeService.uploadNotice(request,notice);
    }

    /**
     * 测试分页查询自己的公告
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


    //==超管查看所有状态的公告==//
    @GetMapping("/findAllNotice/{start}/{size}")
    public ResponseResult findAllNotice(@PathVariable("start")Integer start,@PathVariable("size")Integer size){
        return noticeService.findAllNotice(start,size);
    }


    //审核公告(拒绝和通过)
    @PostMapping("/checkNotice/{noticeID}/{result}")
    public ResponseResult checkNotice(@PathVariable("noticeID") String noticeID,@PathVariable("result") String result){
        return noticeService.checkNotice(noticeID,result);
    }

}
