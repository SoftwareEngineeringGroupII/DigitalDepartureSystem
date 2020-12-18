package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.*;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICardService;
import com.digitaldeparturesystem.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service  //一卡通
@Transactional
public class CardServiceImpl implements ICardService {


    @Autowired
    private IdWorker idWorker;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private EduMapper eduMapper;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private CardMapper cardMapper;

    //-----2020.11.24 周二增加部分---- //

    @Value("${photo.dir}")
    private String realPath;

    /**
     * 上传公告:图片字段
     * @param notice
     * @param photo
     * @return
     */
    @Override
    public ResponseResult uploadNotice(Notice notice, MultipartFile photo,
                                       HttpServletRequest request,HttpServletResponse response) throws IOException {

        //获取当前用户信息
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseClerkByTokenKey(redisUtils, tokenKey);
       // log.info("职工姓名"+clerk.getClerkName()+"");

        //检查数据:标题、内容不可以为空
        if (TextUtils.isEmpty(notice.getTitle())) {
            ResponseResult.FAILED("标题不可以为空！");
        }

        if (TextUtils.isEmpty(notice.getContent())) {
            ResponseResult.FAILED("公告内容不可以为空！");
        }

        log.info("公告信息：[{}]",notice.toString());
        log.info("公告图片：[{}]",photo.getOriginalFilename());
        try{
            String newFileName = idWorker.nextId() + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
            photo.transferTo(new File(realPath, newFileName));//保存到指定的路径文件路径中
            //设置数据
            Notice newNotice = new Notice();
            newNotice.setPath(newFileName);//图片路径
            newNotice.setNoticeID(idWorker.nextId()+"");//公告ID
            newNotice.setTitle(notice.getTitle());//标题
            newNotice.setContent(notice.getContent());//内容
            newNotice.setRemark(notice.getRemark());//设置备注
            newNotice.setPublisherId(clerk.getClerkID());//获取当前发布者ID
            newNotice.setNoticeType(clerk.getDepartment()); //获取当前发布者部门
            newNotice.setCheckStatus("0"); //默认未审核
            newNotice.setIsTop(notice.getIsTop()); //默认非置顶:这里前端有个选择置不置顶,但是好像是超级管理员的事情
            newNotice.setPublishTime(new Date());//发布时间

            //保存数据
            noticeMapper.save(newNotice);

        }catch (Exception e){
            return ResponseResult.FAILED("上传公告失败!");
        }
        //返回结果
        return ResponseResult.SUCCESS("公告添加成功");
    }


/*
    */
/**
     *  分页查询所有学生的所有信息(仅限于学生表)
     * @param map
     * @return
     *//*

    @Override
    public ResponseResult findAllByPage(Map<String,Object> map) {
        Integer page = (Integer)map.get("page");
        Integer size = (Integer)map.get("size");

        //pageHelper使用
        PageHelper.startPage(page,size);
        List<Student> userList = studentMapper.getStudentList();
        PageInfo<Student> studentPageInfo = new PageInfo<>(userList);

        int pageNum = studentPageInfo.getPageNum();
        int pages = studentPageInfo.getPages();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("list",userList);
        map1.put("pageNum",pageNum);
        map1.put("pages",pages);
        return ResponseResult.SUCCESS("查询成功").setData(map1);
    }
*/



    /**
     *  按学号查询一卡通详情
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForCard(String stuNumber) {
        //先查询是否有该学生存在
        Student stuByStuNumber = cardMapper.findStuByStuNumber(stuNumber);
        if (stuByStuNumber == null) {
            return ResponseResult.FAILED("查询失败,没有这个学生存在！");
        }
        //再查询是否有该生的一卡通信息
        Map<String, Object> studentByIdForCard = cardMapper.getStudentByIdForCard(stuNumber);
        if (studentByIdForCard == null) {
            return  ResponseResult.FAILED("查找失败！没有该学生的一卡通详情！");
        }

        List<Map<String,Object>> list = new ArrayList<>();
        list.add(studentByIdForCard);
        return ResponseResult.SUCCESS("查找成功").setData(list);
    }


    /**
     *  审核一卡通 == 旧
     * @param stuNumber
     * @return
     */
    public  ResponseResult doCheckForCard(String stuNumber){
        //修改card表的状态
        cardMapper.doCheckCard(stuNumber);
        //审核成功设置process表cardStatus
        eduMapper.setCardStatus(stuNumber);
        return ResponseResult.SUCCESS("审核成功！");
    }




    @Resource
    private LibraryMapper libraryMapper;

    /**
     * 一卡通审核完后向学生发送消息
     * @param stuNumber
     */
    public void sendMessageForLib(String stuNumber){
        Double cardBalance = cardMapper.cardBalance(stuNumber);
        Message message = new Message();
        message.setTitle("一卡通审核通知");
        if (cardBalance>0){
            message.setContent("一卡通注销成功,余额"+cardBalance+"元,请到财务处结清账款");
        }else {
            message.setContent("一卡通注销成功");
        }
        message.setMessageID(idWorker.nextId()+"");
        message.setSendID(libraryMapper.findStuIDByNumber(stuNumber));
        message.setReceiveID("一卡通");
        message.setMsgStatus("1");//学生端查看
        //保存消息到数据库
        cardMapper.sendMessage(message);
    }




    /**
     * 审核一卡通 == 新
     * @param stuNumber
     * @return
     */
    public ResponseResult checkCard(String stuNumber){
        //修改财务处的一卡通余额
        cardMapper.cardForFinance(stuNumber);
        //向学生端发送信息
        sendMessageForLib(stuNumber);
        //修改card表审核状态
        cardMapper.doCheckCard(stuNumber);
        //修改process表状态
        eduMapper.setCardStatus(stuNumber);
        //修改finance里面的expense
        sumExpense(stuNumber);

        return ResponseResult.SUCCESS("一卡通注销成功");
    }

    @Resource
    private FinanceMapper financeMapper;
    /**
     * 计算总金额设置到finance表中
     * @param stuNumber
     */
    public void sumExpense(String stuNumber){
        financeMapper.sumExpense(stuNumber);
    }



    /**
     * 按条件分页查询一卡通信息
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param cardStatus
     * @return
     */
    public  ResponseResult findAllByPageAndType(Integer start,Integer size,String stuDept,String stuType,String cardStatus){
        //判断类型,如果是所有类型的状态将其置空
        stuDept = (stuDept.equals("所有学院") ?"":stuDept);
        stuType =(stuType.equals("所有学生")?"":stuType);
        cardStatus = (cardStatus.equals("所有状态")?"":cardStatus);

        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("cardStatus",cardStatus);
        log.info("params --- >> "+params);

        if(size==0){
            //如果未设置显示条数，默认为5
            size=5;
        }

        //pageHelper使用
        //分页处理,显示第start页的size条数据
        PageHelper.startPage(start,size);
        List<CardInfo> students = cardMapper.listStudentCardInfos(params);
        PageInfo<CardInfo> cardPageInfo = new PageInfo<>(students);
        int pageNum = cardPageInfo.getPageNum();
        int pages = cardPageInfo.getPages();
        long total = cardPageInfo.getTotal();//获取记录总数
        if (students.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("list",students);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);

        return ResponseResult.SUCCESS("查询成功").setData(map);

    }



    /**
     *  导出所有财务信息
     * @param response
     */
    public void exportAllCard(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有信息
        List<CardInfo> cardInfos = cardMapper.listAllCard();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),CardInfo.class,cardInfos);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("一卡通审核表","UTF-8")+".xls");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *  查询所有一卡通数据
     * @return
     */
    @Override
    public ResponseResult selectAll() {
        List<CardInfo> cardInfos = cardMapper.listAllCard();
        if (cardInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }

        return ResponseResult.SUCCESS("查询成功").setData(cardInfos);
    }

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 查询所有一卡通情况
     * @return
     */
    public ResponseResult selectAll(HttpServletRequest request){

        /*
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        Clerk clerk = TokenUtils.parseByTokenKey(redisUtils, tokenKey);
        log.info("职工姓名"+clerk.getClerkName()+"");
        log.info("职工用户名"+clerk.getUsername()+"");
        */

        List<CardInfo> cardInfos = cardMapper.listAllCard();
        if (cardInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        return ResponseResult.SUCCESS("查询一卡通成功").setData(cardInfos);
    }


    /**
     * 分页查询所有
     * @return
     */
    public ResponseResult findAllByPage(Integer start,Integer size){
        if (size == 0){
            size = 5;
        }

        PageHelper.startPage(start,size);
        List<CardInfo> cardInfos = cardMapper.listAllCard();
        if (cardInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        PageInfo<CardInfo> cardInfoPageInfo = new PageInfo<>(cardInfos);
        long total = cardInfoPageInfo.getTotal();//总记录数
        int pageNum = cardInfoPageInfo.getPageNum();//当前页
        int pages = cardInfoPageInfo.getPages();//总页数
        int pageSize = cardInfoPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",cardInfos);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);//总页数
        map.put("pageSize",pageSize);

        return ResponseResult.SUCCESS("查询成功").setData(map);
    }






    public static final String imagePath="C://Users//Administrator//Desktop//lixiaosys//DigitalDepartureSystem//src//main//resources//static//photos";

    /**
     *  上传文件/图片
     * @param file
     * @return
     */
    public ResponseResult uploadImage(MultipartFile file)  {
        //判断文件类型
        if (file == null) {
            return ResponseResult.FAILED("文件不可以为空！");
        }
        //只支持文件上传,比如png,jpg,gif
        String contentType = file.getContentType();
        if (TextUtils.isEmpty(contentType)) {
            return ResponseResult.FAILED("图片格式错误");
        }
        log.info("该文件类型是===>>> "+contentType);
        if (!"image/png".equals(contentType)&&
              !"image/gif".equals(contentType)&&
                !"image/jpg".equals(contentType)&&
                !"image/jpeg".equals(contentType)) {
            return ResponseResult.FAILED("不支持此图片类型");
        }
        //获取相关数据,如文件类型,文件名称
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename == > "+originalFilename);
        //根据定义的规则进行命名
        File targetFile = new File(imagePath + File.separator + originalFilename);
        log.info("target == >> "+targetFile);
        //保存文件
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.FAILED("图片上传失败,请稍后重试");
        }

        //记录文件
        //返回结果
        return ResponseResult.SUCCESS("图片上传成功");
    }

    /**
     * 显示图片
     * @param response
     * @param imageId
     * @return
     */
    public void viewImage(HttpServletResponse response,String imageId) throws IOException {
        File file = new File(imagePath + File.separator + "144.png");

        OutputStream writer = null;
        FileInputStream fos = null;
        try {
            writer = response.getOutputStream();
            //读取
            fos = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while((len = fos.read(buff))!= -1){
                writer.write(buff,0,len);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                writer.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }









}
