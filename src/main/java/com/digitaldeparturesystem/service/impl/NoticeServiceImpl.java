package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.NoticeMapper;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.INoticeService;
import com.digitaldeparturesystem.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service  //图书馆
@Transactional
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private NoticeMapper noticeMapper;

    @Autowired
    private IdWorker idWorker;


    //==普通管理员===//

    /**
     * zy
     * 上传公告(纯表单)
     * @return
     */
    public ResponseResult uploadNotice(HttpServletRequest request, Notice notice){
        //获取当前用户信息
        String cookie = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseClerkByTokenKey(redisUtils, cookie);

        log.info("当前用户信息 === > > "+clerk.getUserRoles()+clerk.getClerkName());

        String clerkID = clerk.getClerkID();
        List<Role> userRoles = clerk.getUserRoles();
        //检查数据：标题、内容不可以为空
        if (TextUtils.isEmpty(notice.getTitle())) {
            return ResponseResult.FAILED("标题不可以为空！");
        }
        if (TextUtils.isEmpty(notice.getContent())) {
            return ResponseResult.FAILED("公告内容不可以为空！");
        }
        log.info("公告信息：[{}]",notice);
        notice.setNoticeID(idWorker.nextId()+"");//公告的主键
        notice.setPublisherId(clerkID);//发布者账号
        notice.setPublishTime(new Date());//发布时间
        notice.setCheckStatus("0");//默认未设置
        notice.setNoticeType(clerk.getDepartment());//当前发布部门
        notice.setIsTop("0");//是否置顶,默认非置顶
      //  notice.setRemark("");
        //保存数据
        noticeMapper.save(notice);

        return ResponseResult.SUCCESS("新增公告成功");
    }



    /**
     * zy
     * 查看自己的公告
     * @return
     */
    public ResponseResult findSelfNotice(HttpServletRequest request,Integer start,Integer size){
        //获取当前用户信息
        String cookie = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseClerkByTokenKey(redisUtils, cookie);
        //String clerkID = clerk.getClerkID();//当前用户id
        String department = clerk.getDepartment();


        PageHelper.startPage(start,size);
        List<Notice> notices = noticeMapper.listSelfNotice(department);
        PageInfo<Notice> noticePageInfo = new PageInfo<>(notices);
        long total = noticePageInfo.getTotal();
        int pageNum = noticePageInfo.getPageNum();
        int pages = noticePageInfo.getPages();
        int pageSize = noticePageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",notices);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("pageSize",pageSize);
        return ResponseResult.SUCCESS("查询成功").setData(map);
    }

    /**
     * zy
     * 保存草稿箱
     * @param notice
     * @return
     */
    public ResponseResult draftNotice(HttpServletRequest request,Notice notice){
        //获取当前用户信息
        String cookie = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseClerkByTokenKey(redisUtils, cookie);
        log.info("当前用户信息 === > > "+clerk.getUserRoles()+clerk.getClerkName());
        String clerkID = clerk.getClerkID();

        //检查数据：标题、内容不可以为空
        if (TextUtils.isEmpty(notice.getTitle())) {
            return ResponseResult.FAILED("标题不可以为空！");
        }
        if (TextUtils.isEmpty(notice.getContent())) {
            return ResponseResult.FAILED("公告内容不可以为空！");
        }
        log.info("公告信息：[{}]",notice);
        notice.setNoticeID(idWorker.nextId()+"");//公告的主键
        notice.setPublisherId(clerkID);//发布者账号
        notice.setPublishTime(new Date());//发布时间
        notice.setCheckStatus("3");//草稿状态
        notice.setNoticeType(clerk.getDepartment());//当前发布部门
        notice.setIsTop("0");//是否置顶,默认非置顶
        //  notice.setRemark("");
        //保存数据
        noticeMapper.save(notice);

        return ResponseResult.SUCCESS("已成功保存至草稿箱");
    }


    /**
     * zy
     * 继续编辑
     * @param noticeID
     * @return
     */
    public ResponseResult continueNotice(String noticeID){
        Notice notice = noticeMapper.viewNoticeDetails(noticeID);
        List<Notice> list = new ArrayList<>();
        list.add(notice);
        return ResponseResult.SUCCESS().setData(list);

    }

    /**
     * 标题搜索公告
     * @param title
     * @return
     */
    public ResponseResult searchNoticeByTitle(HttpServletRequest request,String title,Integer start,Integer size){
        //获取当前用户信息
        String cookie = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseClerkByTokenKey(redisUtils, cookie);
      //  log.info("当前用户信息 === > > "+clerk.getUserRoles()+clerk.getClerkName());
      //  String clerkID = clerk.getClerkID();
        String department = clerk.getDepartment();

        PageHelper.startPage(start,size);
        List<Notice> notices = noticeMapper.searchNoticeByTitle(department, title);
        PageInfo<Notice> objectPageInfo = new PageInfo<>(notices);
        if (notices.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        long total = objectPageInfo.getTotal();
        int pageNum = objectPageInfo.getPageNum();
        int pages = objectPageInfo.getPages();
        int pageSize = objectPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list", notices);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("pageSize",pageSize);
        return ResponseResult.SUCCESS("查询成功").setData(map);


    }







    //====公共页面公共显示=====//

    /**
     * zy
     * 显示在首页的所有(已审核通过的)
     * @return
     */
    public ResponseResult viewByAllPeople() throws ParseException {
       //List<Notice> notices = noticeMapper.viewAllByPeople();
        List<Notice> notices1 = noticeMapper.viewTopNotice();
        List<Notice> notices2 = noticeMapper.viewUnTopNotice();

        notices1.addAll(notices2);
      /*  for(int i=0;i<notices1.size();i++)    {
            Notice notice = notices1.get(i);
            Date publishTime = notice.getPublishTime();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = format.format(publishTime);

        }*/
        if (notices1.isEmpty()) {
            return ResponseResult.FAILED("没有公告信息");
        }
        return ResponseResult.SUCCESS("查询公告成功").setData(notices1);
    }



    /**
     * zy
     * 首页查询公告详情
     * @param noticeID
     * @return
     */
    public ResponseResult viewNoticeDetail(String noticeID){
        Notice notice = noticeMapper.viewNoticeDetails(noticeID);
        List<Notice> list  = new ArrayList<>();
        list.add(notice);
        return ResponseResult.SUCCESS("查看公告详情成功").setData(list);
    }








    //====超管部分=====///


    /**
     * zy
     *  超管
     *  审核公告
     *  是否显示在首页(通过-1;拒绝-2;未审核-0)
     *  是否置顶
     * @return
     */
    public ResponseResult checkNotice(String noticeID,String result){
        //如果通过,设置为1
        if ("1".equals(result)){
            noticeMapper.setNoticePass(noticeID);
            return ResponseResult.SUCCESS("通过成功");
        }
        //拒绝通过
        noticeMapper.setNoticeRefuse(noticeID);
        return ResponseResult.SUCCESS("拒绝成功");
    }


    /**
     * zy
     * 删除公告
     * @return
     */
    public ResponseResult deleteNotice(String noticeID){
        noticeMapper.deleteNotice(noticeID);
        return ResponseResult.SUCCESS("删除公告成功");
    }

    /**
     * 所有数据
     * @return
     */
    public List<Notice> allNotice(){
        List<Notice> notices = noticeMapper.NoCheckNotice();
        List<Notice> notices1 = noticeMapper.haveCheckNotice();
        List<Notice> notices2 = noticeMapper.RefuseNotice();
        notices.addAll(notices2);
        notices.addAll(notices1);
        return notices;
    }



    /**
     * zy
     * 超管查询所有的公告--分页展示
     * @return
     */
    public ResponseResult findAllNotice(Integer start,Integer size){
        if (size == 0) {
            size = 5;
        }
        /*List<Notice> notices = noticeMapper.NoCheckNotice();
        List<Notice> notices1 = noticeMapper.haveCheckNotice();
        List<Notice> notices2 = noticeMapper.RefuseNotice();
        notices.addAll(notices2);
        notices.addAll(notices1);*/

        PageHelper.startPage(start,size);
        List<Notice> notices = noticeMapper.listAllNotice();
        PageInfo<Notice> noticePageInfo = new PageInfo<>(notices);

        long total = noticePageInfo.getTotal();
        int pages = noticePageInfo.getPages();
        int pageNum = noticePageInfo.getPageNum();
        Map<String, Object> map = new HashMap<>();
        map.put("list",notices);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);

        return ResponseResult.SUCCESS("查询成功").setData(map);

    }


    /**
     * zy
     * 设置公告置顶
     * @param noticeID
     * @return
     */
    public ResponseResult setTop(String noticeID){
        noticeMapper.setTop(noticeID);
        return ResponseResult.SUCCESS("设置公告置顶成功");
    }

    /**
     * zy
     * 取消公告置顶
     * @param noticeID
     * @return
     */
    public ResponseResult setUnTop(String noticeID){
        noticeMapper.setUnTop(noticeID);
        return ResponseResult.SUCCESS("取消公告置顶成功");
    }


    /**
     * zy
     * 超管上传公告
     * @param request
     * @param notice
     * @return
     */
    public  ResponseResult uploadNoticeBySuper(HttpServletRequest request,Notice notice){
        //获取当前用户信息
        String cookie = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseClerkByTokenKey(redisUtils, cookie);

        log.info("当前用户信息 === > > "+clerk.getUserRoles()+clerk.getClerkName());

        String clerkID = clerk.getClerkID();
        List<Role> userRoles = clerk.getUserRoles();
        //检查数据：标题、内容不可以为空
        if (TextUtils.isEmpty(notice.getTitle())) {
            return ResponseResult.FAILED("标题不可以为空！");
        }
        if (TextUtils.isEmpty(notice.getContent())) {
            return ResponseResult.FAILED("公告内容不可以为空！");
        }
        log.info("公告信息：[{}]",notice);
        notice.setNoticeID(idWorker.nextId()+"");//公告的主键
        notice.setPublisherId(clerkID);//发布者账号
        notice.setPublishTime(new Date());//发布时间
        notice.setCheckStatus("1");//默认未设置
        notice.setNoticeType(clerk.getDepartment());//当前发布部门
        notice.setIsTop("1");//是否置顶,超管默认置顶
        notice.setRemark("");
        //保存数据
        noticeMapper.save(notice);

        return ResponseResult.SUCCESS("新增公告成功");
    }

    /**
     * 搜索公告(下拉按部门名搜索)==分页
     * @param department
     * @return
     */
    public ResponseResult searchNotice(String department,Integer start,Integer size){
        if (size == 5) {
            size = 5;
        }
        PageHelper.startPage(start,size);
        List<Notice> notices = noticeMapper.listByDepartment(department);
        PageInfo<Notice> noticePageInfo = new PageInfo<>(notices);
        int pageNum = noticePageInfo.getPageNum();
        int pages = noticePageInfo.getPages();
        long total = noticePageInfo.getTotal();

        Map<String,Object> map = new HashMap<>();
        map.put("list",notices);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);

        return ResponseResult.SUCCESS("查询公告成功").setData(map);
    }


    /**
     *  上传文件
     * @param file
     * @return
     */
    public ResponseResult handleFileUpload(MultipartFile file) {
        File checkFile = new File("files/");
        if (!checkFile.exists()){
            checkFile.mkdirs();
        }
        String newFileName=null;
        if (!file.isEmpty()) {
            BufferedOutputStream out = null;
            try {
                File file1 = new File(
                        "files" + File.separator + idWorker.nextId() + file.getOriginalFilename());
                newFileName=file1.getName();
                out = new BufferedOutputStream(
                        new FileOutputStream(file1));
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
            return ResponseResult.SUCCESS("上传文件成功").setData(newFileName);
        } else {
            return ResponseResult.FAILED("文件为空");
        }
    }


    /**
     * 下载文件
     * @param filename
     * @throws IOException
     */
    public void download(String filename) throws IOException {
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
