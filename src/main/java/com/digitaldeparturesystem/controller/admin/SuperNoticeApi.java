package com.digitaldeparturesystem.controller.admin;


import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.INoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/admin/notice")
public class SuperNoticeApi {

    @Autowired
    private INoticeService noticeService;

    //====//

    /**
     * zy
     * 超管查看所有状态的公告====列表分页
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllNotice/{start}/{size}")
    public ResponseResult findAllNotice(@PathVariable("start")Integer start, @PathVariable("size")Integer size){
        return noticeService.findAllNotice(start,size);
    }



    /**
     * zy
     * 审核公告(拒绝和通过)
     * @param noticeID
     * @param result
     * @return
     */
    @PostMapping("/checkNotice/{noticeID}/{result}")
    public ResponseResult checkNotice(@PathVariable("noticeID") String noticeID,@PathVariable("result") String result){
        return noticeService.checkNotice(noticeID,result);
    }


    /**
     * zy
     * 设置公告置顶
     * @param noticeID
     * @return
     */
    @PostMapping("/setTop/{noticeID}")
    public ResponseResult setTop(@PathVariable("noticeID")String noticeID){
        return noticeService.setTop(noticeID);
    }


    /**
     * zy
     *  取消公告置顶
     * @param noticeID
     * @return
     */
    @PostMapping("/setUnTop/{noticeID}")
    public ResponseResult setUnTop(@PathVariable("noticeID")String  noticeID){
        return noticeService.setUnTop(noticeID);
    }

    /**
     * zy
     * 删除公告
     * @param noticeID
     * @return
     */
    @PostMapping("/deleteNotice/{noticeID}")
    public ResponseResult deleteNotice(@PathVariable("noticeID")String  noticeID){
        return noticeService.deleteNotice(noticeID);
    }


    /**
     *  zy
     *  超管查看公告详情
     * @param noticeID
     * @return
     */
    @GetMapping("/viewNoticeDetail/{noticeID}")
    public ResponseResult viewNoticeDetail(@PathVariable("noticeID")String  noticeID){
        return noticeService.viewNoticeDetail(noticeID);
    }



    /**
     * zy
     * 超管上传自己的公告
     * @param request
     * @param notice
     * @return
     */
    @PostMapping("/uploadNotice")
    public  ResponseResult uploadNotice(HttpServletRequest request, @RequestBody Notice notice){
        return noticeService.uploadNoticeBySuper(request,notice);
    }



    /**
     * zy
     * 按部门 == 搜索公告 分页
     * @param department
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/searchNotice/{start}/{size}")
    public ResponseResult searchNotice(@RequestParam String department,@PathVariable("start") Integer start,
                                       @PathVariable("size") Integer size){
          return noticeService.searchNotice(department,start,size);
    }




}
