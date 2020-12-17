package com.digitaldeparturesystem.controller.common;


import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.INoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/common/notice")
public class CommonNoticeApi {

    @Autowired
    private INoticeService noticeService;

    /**
     * zy
     * 公共页面显示(置顶+非置顶)
     * @return
     */
    @GetMapping("/viewByAllPeople")
    public ResponseResult viewAllByPeople()throws Exception{
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



}
