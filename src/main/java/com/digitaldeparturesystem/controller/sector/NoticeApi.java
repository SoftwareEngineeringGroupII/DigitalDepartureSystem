package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICardService;
import com.digitaldeparturesystem.service.ISectorService;
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
    private ISectorService sectorService;

    @Autowired
    private ICardService cardService;
    /**
     * 上传公告
     * 识别上传公告人权限,ID,发布类型,
     * @param notice
     * @param photo
     * @return
     */
    @PostMapping("/notice")
    public ResponseResult uploadNotice(@RequestBody Notice notice, MultipartFile photo,HttpServletRequest request) throws IOException {

        return cardService.uploadNotice(notice,photo,request);
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
    @PostMapping("/top/{noticeId}")
    public ResponseResult topNotice(@PathVariable("noticeId")String articleId){
        return null;
    }
}
