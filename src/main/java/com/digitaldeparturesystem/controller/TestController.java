package com.digitaldeparturesystem.controller;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.RedisUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ISectorService sectorService;

    @GetMapping("/hello_world")
    public ResponseResult helloWorld(){
        System.out.println("hello world");
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setCode(20000);
        responseResult.setMessage("操作成功");
        responseResult.setData("hello world");
        log.info("hello world!!!");
        return responseResult;
    }


    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        //specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        specCaptcha.setFont(Captcha.FONT_1);
        // 设置类型，纯数字、纯字母、字母数字混合
        //specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        String content = specCaptcha.text().toLowerCase();
        log.info("content ==> " + content);
        // 验证码存入session
        request.getSession().setAttribute("captcha", content);
        //保存redis里
        //key value time: 60*10,10分钟内有效
        redisUtils.set(Constants.Clerk.KEY_CAPTCHA_CONTENT + "123456", content, 60 * 10);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    /**
     * 测试double token
     * @return
     */
    @GetMapping("/checkClerk")
    public ResponseResult login() {
        Clerk clerk = sectorService.checkClerk();
        System.out.println("clerk.getClerkName() --> " + clerk.getClerkName());
        System.out.println("clerk.getDepartment() --> " + clerk.getDepartment());
        System.out.println("clerk.getClerkAccount() --> " + clerk.getClerkAccount());
        return ResponseResult.SUCCESS("检查成功");
    }

}
