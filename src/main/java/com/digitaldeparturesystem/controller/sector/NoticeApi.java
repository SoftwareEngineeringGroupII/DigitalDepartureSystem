package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sector/notice")
public class NoticeApi {

    /**
     * 添加公告
     * @param notice
     * @return
     */
    @PostMapping
    public ResponseResult addNotice(@RequestBody Notice notice){
        return null;
    }

    /**
     * 删除公告
     * @param noticeId
     * @return
     */
    @DeleteMapping("/{noticeId}")
    public ResponseResult deleteNotice(@PathVariable("noticeId")String noticeId){
        return null;
    }

    /**
     * 得到某一个公告
     * @param noticeId
     * @return
     */
    @GetMapping("/{noticeId}")
    public ResponseResult getNoticeById(@PathVariable("noticeId")String noticeId){
        return null;
    }

    /**
     * 得到全部公告
     * @return
     */
    @GetMapping()
    public ResponseResult getAllNotices(){
        return null;
    }

    /**
     * 置顶公告
     * @param articleId
     * @return
     */
    @PutMapping("/top/{noticeId}")
    public ResponseResult topNotice(@PathVariable("noticeId")String articleId){
        return null;
    }
}
