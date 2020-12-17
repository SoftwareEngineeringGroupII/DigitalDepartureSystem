package com.digitaldeparturesystem.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.CardMapper;
import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.FinanceInfo;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.FileUtil;
import com.digitaldeparturesystem.utils.RedisUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

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

    @Resource
    FinanceMapper financeMapper;

    @Resource
    CardMapper cardMapper;
    /**
     *  测试导出Excel功能
     * @return
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有数据
        List<FinanceInfo> list = financeMapper.listNoCheck();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), FinanceInfo.class,list);

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("财务处审核表","UTF-8")+".xsl");
        response.setCharacterEncoding("UTF-8");

        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult handleFileUpload(@RequestParam("file") MultipartFile file) {
        File checkFile = new File("files/");
        if (!checkFile.exists()){
            checkFile.mkdirs();
        }
        if (!file.isEmpty()) {
            BufferedOutputStream out = null;
            try {
                out = new BufferedOutputStream(
                        new FileOutputStream(new File(
                                "files"+File.separator+file.getOriginalFilename())));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
            } catch (FileNotFoundException e) {
                return ResponseResult.FAILED("上传失败");
            } catch (IOException e) {
                return ResponseResult.FAILED("上传失败");
            }finally {
                if (out!=null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ResponseResult.SUCCESS("上传文件成功");
        } else {
            return ResponseResult.FAILED("文件为空");
        }
    }


    @RequestMapping(value = "download")
    public void download(
            @RequestParam("fileName") String filename
    ) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(filename);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("Content-type",type);
        // 设置编码
        String hehe = new String(filename.getBytes("utf-8"), "iso-8859-1");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        FileUtil.download(filename, response);
    }






}
