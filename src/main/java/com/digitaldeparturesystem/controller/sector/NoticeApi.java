package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeApi {

    @Autowired
    private ISectorService sectorService;

    /**
     * 上传公告
     * 识别上传公告人权限,ID,发布类型,
     * @param notice
     * @param photo
     * @param request
     * @return
     */
    @PostMapping("/notice")
    public ResponseResult uploadNotice(@RequestBody Notice notice, MultipartFile photo,
                                       HttpServletRequest request, HttpServletResponse response) throws IOException {

        return sectorService.uploadNotice(notice,photo,request,response);
    }


}
