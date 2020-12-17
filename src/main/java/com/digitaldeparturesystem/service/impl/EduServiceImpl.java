package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IEduService;
import com.digitaldeparturesystem.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service //教务处
@Transactional
public class EduServiceImpl implements IEduService {

    @Resource
    private EduMapper eduMapper;

    /**
     *  导出所有学生基本信息
     * @param response
     */
    public void exportAllStuBasicInfo(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有信息
        List<StuBasicInfo> maps = eduMapper.exportAllStuBasicInfo();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),StuBasicInfo.class,maps);

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("学生基本信息表","UTF-8")+".xls");
        response.setCharacterEncoding("UTF-8");
        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    /**
     * 分页查询所有的离校最终审核状态
     * @return
     */
    public ResponseResult selectAllByPage(Integer start, Integer size){
        if (size == null) {
            size = 5;
        }
        PageHelper.startPage(start,size);
        List<EduInfo> eduInfos = eduMapper.listAllEdu();
        PageInfo<EduInfo> mapPageInfo = new PageInfo<>(eduInfos);
        long total = mapPageInfo.getTotal();
      //  log.info("total的值为：---->>> "+total);
        int pageNum = mapPageInfo.getPageNum();
        int pages = mapPageInfo.getPages();
        int pageSize = mapPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",eduInfos);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("pageSize",pageSize);
        return ResponseResult.SUCCESS("查询成功").setData(map);
    }


    /**
     * 根据学号查询离校状态--主页显示
     * @param stuNumber
     * @return
     */
    public ResponseResult getStudentByIdForEdu(String stuNumber){
        //先查询是否有该学生存在
        Student stuByStuNumber = eduMapper.findStuByStuNumber(stuNumber);
        if (stuByStuNumber == null) {
            return ResponseResult.FAILED("没有这个学生存在！请重新输入学号");
        }
        //再查看有没有该学生的离校信息
        //最后成功返回信息
        EduInfo studentByIdForEdu = eduMapper.getStudentByIdForEdu(stuNumber);
        List<EduInfo> list = new ArrayList<>();
        list.add(studentByIdForEdu);
        return ResponseResult.SUCCESS("查询成功").setData(list);
    }

    /**
     *  根据学号查询学生教务处 -- 详情
     * @param stuNumber
     * @return
     */
    public ResponseResult findStuDetailForEdu(String stuNumber){
        //查询学校离校流程进度
        Process stuProcess = eduMapper.getStuProcess(stuNumber);
        //查询学生基本信息
        StuBasicInfo stuBasicInfo = eduMapper.getStuBasicInfo(stuNumber);

       List<StuBasicInfo> stuInfo = new ArrayList<>();
       stuInfo.add(stuBasicInfo);
       List<Process> process = new ArrayList<>();
       process.add(stuProcess);
       Map<String,Object> map = new HashMap<>();
       map.put("stuInfo",stuInfo);
       map.put("process",process);

       return ResponseResult.SUCCESS("查询成功").setData(map);

    }

    /**
     * 查看学生申请的表单信息
     * @return
     */
    public ResponseResult viewMessage(String stuNumber){
        Map<String, Object> map = eduMapper.viewMessage(stuNumber);
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return ResponseResult.SUCCESS("查看学生申请表单信息成功").setData(list);
    }


    /**
     *  拒绝某个学生的最终离校 refuse
     * @return
     */
    public ResponseResult doCheckForEduRefuse(String stuNumber,Message message){
        //如果审核被拒绝
        //1.填写反馈信息表单
        String title = message.getTitle();//获取标题
        String content = message.getContent();//获取内容
       /* log.info("title == >> "+title);
        log.info("content == >> "+content);*/
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("反馈标题不能为空");
        }
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.FAILED("反馈内容不能为空");
        }
        //2.覆盖消息
      /*
       //写法一：查找数据库此人信息记录
        Message message1 = eduMapper.findMessage(stuNumber);
        //设置反向反馈信息
        message1.setContent(content);
        message1.setTitle(title);
        message1.setMessagedate(new Date());
        eduMapper.setMessage(stuNumber,message1);
        */
        //写法二：覆盖内容,并且设置审核信息状态1
        Date time = new Date();
        eduMapper.setMessage(stuNumber,content,title,time);
        return ResponseResult.SUCCESS("拒绝成功");
    }


    /**
     * 通过某个学生的离校申请 Pass
     * @return
     */
    public  ResponseResult doCheckForEduPass(String stuNumber){
        //先判断学生满足离校的条件
        //1.离校进度流程是否走完
        Process stuProcess = eduMapper.getStuProcess(stuNumber);
        String cardStatus = stuProcess.getCardStatus();
        String financeStatus = stuProcess.getFinanceStatus();
        String dormStatus = stuProcess.getDormStatus();
        String libStatus = stuProcess.getLibStatus();
       /* log.info("cardStatus == >> "+cardStatus);
        log.info("financeStatus == >> "+financeStatus);
        log.info("dormStatus == >> "+dormStatus);
        log.info("libStatus == >> "+libStatus);*/
        if (!"1".equals(cardStatus)&& !"1".equals(dormStatus)&&
                !"1".equals(financeStatus)&& !"1".equals(libStatus)
            ){
            return ResponseResult.FAILED("离校手续未办理完善！请先进行相关手续办理");
        }
        //2. 判断学分是否已经修满：这里暂时默认为200
        int stuCredit =Integer.parseInt(eduMapper.findStuCredit(stuNumber));
        if (stuCredit<200){
            return ResponseResult.FAILED("学分不达要求,不能通过审核");
        }
        //设置message状态
        Date time = new Date();
        eduMapper.setMessage(stuNumber,"已通过离校申请","教务处",time);
        //设置edu状态
        eduMapper.setProcessEdu(stuNumber);
        //设置process的edu状态
        eduMapper.doCheckEdu(stuNumber);
        //设置student表学生离校时间
        eduMapper.setStuOutDate(stuNumber,new Date());
        return ResponseResult.SUCCESS("审核通过成功");
    }



 /*   public ResponseResult testSelectAll(){
        List<Map<String, Object>> maps = eduMapper.listPostEdu();
        List<Map<String, Object>> maps1 = eduMapper.listNoPostEdu();
        maps.addAll(maps1);
        return ResponseResult.SUCCESS().setData(maps);
    }*/

    /**
     * 按条件分页查询教务处 ==
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param eduStatus
     * @return
     */
    public ResponseResult findAllByPageAndType(Integer start, Integer size,
                                               String stuDept, String stuType, String eduStatus) {

        //判断类型,如果为所有类型都置空
        stuDept = (stuDept.equals("所有学院")?"":stuDept);
        stuType = (stuType.equals("所有学生")?"":stuType);
        eduStatus = (eduStatus.equals("所有状态")?"":eduStatus);

        //将请求参数放入param中
        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("eduStatus",eduStatus);
        log.info("param ---->>> "+params);
      /*  //如果未设置显示条数，默认为5
        if (size == 0) {
            size = 5;
        }
        List<EduInfo> eduInfos = eduMapper.listPostEdu();
        List<EduInfo> eduInfos1 = eduMapper.listNoPostEdu();
        eduInfos.addAll(eduInfos1);*/
        //PageHelper处理分页
        //显示第start也的size条数据
        PageHelper.startPage(start,size);
       // List<Map<String, Object>> students = dormMapper.listStudentDormInfos(params);
        List<EduInfo> eduInfos = eduMapper.listStudentEduInfos(params);
        PageInfo<EduInfo> eduInfoPageInfo = new PageInfo<>(eduInfos);
        int pageNum = eduInfoPageInfo.getPageNum();
        int pages = eduInfoPageInfo.getPages();
        long total = eduInfoPageInfo.getTotal();//获取记录总数

        HashMap<Object, Object> map = new HashMap<>();
        map.put("list",eduInfos);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);
        return ResponseResult.SUCCESS("查询成功").setData(map);

    }


    /**
     * 显示所有
     * @return
     */
    public ResponseResult listAll(){
        List<EduInfo> eduInfos = eduMapper.listPostEdu();
        List<EduInfo> eduInfos1 = eduMapper.listNoPostEdu();

        eduInfos.addAll(eduInfos1);
        return ResponseResult.SUCCESS("查询成功").setData(eduInfos);


    }





}
