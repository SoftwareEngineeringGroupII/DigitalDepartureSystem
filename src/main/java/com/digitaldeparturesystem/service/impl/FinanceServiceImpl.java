package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.mapper.NoticeMapper;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFinanceService;
import com.digitaldeparturesystem.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.attoparser.dom.Text;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service  //一卡通
@Transactional
public class FinanceServiceImpl implements IFinanceService {

    @Autowired
    private IdWorker idWorker;

    @Resource
    private FinanceMapper financeMapper;

    @Resource
    private EduMapper eduMapper;

    public ResponseResult uploadNotice(Notice notice, MultipartFile photo){

        return null;
    }



    /**
     *  查询某个学生财务缴费情况
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForFinance(String stuNumber) {
        if (stuNumber == null) {
            return ResponseResult.FAILED("输入学号为空,请重新输入");
        }
        //先查询是否有该学生存在
        Student stuByStuNumber = financeMapper.findStuByStuNumber(stuNumber);
        if (stuByStuNumber == null) {
            return ResponseResult.FAILED("查询失败,没有这个学生存在！");
        }

        //再查询是否有该学生的财务处信息
        FinanceInfo studentByIdForFinance = financeMapper.getStudentByIdForFinance(stuNumber);
        if (studentByIdForFinance==null) {
            return ResponseResult.FAILED("查找失败！没有该学号学生的财务信息");
        }

        List<FinanceInfo> list = new ArrayList<>();
        list.add(studentByIdForFinance);
        return ResponseResult.SUCCESS("查找成功！").setData(list);
    }




    /**
     *  按条件分页查询
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param financeStatus
     * @return
     */
    public ResponseResult findAllByPageAndType(Integer start,Integer size,
                                                String stuDept,String stuType,String financeStatus){

        //判断类型,如果是所有类型的状态将其置空
        stuDept = (stuDept.equals("所有学院") ?"":stuDept);
        stuType =(stuType.equals("所有学生")?"":stuType);
        financeStatus = (financeStatus.equals("所有状态")?"":financeStatus);

        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("financeStatus",financeStatus);
        log.info("params --- >> "+params);

        if(size==0){
            //如果未设置显示条数，默认为5
            size=5;
        }

        //pageHelper使用
        //分页处理,显示第start页的size条数据
        PageHelper.startPage(start,size);
        List<FinanceInfo> students = financeMapper.listStudentFinanceInfos(params);
        PageInfo<FinanceInfo> financePageInfo = new PageInfo<>(students);
        int pageNum = financePageInfo.getPageNum();
        int pages = financePageInfo.getPages();
        long total = financePageInfo.getTotal();//获取记录总数
        if (students.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }

      /*  for (Map<String, Object> student : students) {
            System.out.println(student);
        }
        System.out.println("共有"+total+"条数据");*/


        Map<String,Object> map = new HashMap<>();
        map.put("list",students);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);
        return ResponseResult.SUCCESS("查询成功").setData(map);

    }




    /**
     * zy
     * 财务处审核 根据学生学号：修改审核状态
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult doCheckForFinance(String stuNumber) {
            //先查询其他部门的审核状态：一卡通、财务处、后勤处
        int cardStatus = financeMapper.findCardStatus(stuNumber);
        int dormStatus = financeMapper.findDormStatus(stuNumber);
        int libStatus = financeMapper.findLibStatus(stuNumber);
        //如果有一个部门没结算清楚,不能通过
        if (cardStatus==0||dormStatus==0||libStatus==0){
                return ResponseResult.FAILED("审核失败！请先去其他部门结算赔偿/罚款金额");
        }
        //如果,修改状态
        financeMapper.doCheckForFinance(stuNumber);
        //审核成功后设置离校流程表financeStatus
        eduMapper.setFinanceStatus(stuNumber);
        //审核后将各个明细置为0
        financeMapper.setFine0(stuNumber);
        return ResponseResult.SUCCESS("审核成功");
    }





    /**
     * zy
     *  初始化查询已审核数据
     * @return
     */
    public ResponseResult hadCheck(){
       try {
           List<FinanceInfo> students = financeMapper.listHadCheck();
           if (students.isEmpty()){
               return ResponseResult.FAILED("没有已审核数据信息！");
           }
           Map<String, Object> list = new HashMap<>();
           list.put("students",students);
           return ResponseResult.SUCCESS("查询到数据").setData(list);
       }catch (Exception e){
           return ResponseResult.FAILED("查询数据失败！请重新操作！");
       }
    }

    /**
     * zy
     * 初始化查询未审核数据
     * @return
     */
    public ResponseResult noCheck() {
        try {
            List<FinanceInfo> students = financeMapper.listNoCheck();
            if (students.isEmpty()){
                return ResponseResult.FAILED("没有未审核数据信息！");
            }
            Map<String, Object> list = new HashMap<>();
            list.put("students",students);
            return ResponseResult.SUCCESS("查询到数据").setData(list);
        } catch (Exception e) {
            return ResponseResult.FAILED("查询数据失败！请重新操作！");
        }
    }

    /**
     * zy
     *  导出所有财务信息
     * @param response
     */
    public void exportAllFinance(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有信息
        List<FinanceInfo> financeInfos = financeMapper.listAllFinance();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),FinanceInfo.class,financeInfos);

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("财务处审核表","UTF-8")+".xls");
        response.setCharacterEncoding("UTF-8");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

/*
    public ResponseResult selectAll(){
        List<DormInfo> dormInfos = dormMapper.listAllDorm();
        if (dormInfos.isEmpty()) {
            return ResponseResult.FAILED("查询失败");
        }
        return ResponseResult.SUCCESS("查询成功").setData(dormInfos);
    }*/

    /**
     * zy
     * 查询所有财务情况
     * @return
     */
  public  ResponseResult selectAll(){
      List<FinanceInfo> financeInfos = financeMapper.listAllFinance();
      if (financeInfos.isEmpty()) {
          return ResponseResult.FAILED("查询失败");
      }
      return ResponseResult.SUCCESS("查询成功").setData(financeInfos);

  }

    /**
     * zy
     * 分页查询所有
     * @param start
     * @param size
     * @return
     */
  public ResponseResult findAllByPage(Integer start,Integer size){
      if (size == null) {
          size = 5;
      }
      PageHelper.startPage(start,size);
      List<FinanceInfo> financeInfos = financeMapper.listAllFinance();
      if (financeInfos.isEmpty()) {
          return ResponseResult.FAILED("没有数据");
      }
      PageInfo<FinanceInfo> financeInfoPageInfo = new PageInfo<>(financeInfos);
      long total = financeInfoPageInfo.getTotal();
      int pageNum = financeInfoPageInfo.getPageNum();
      int pages = financeInfoPageInfo.getPages();
      int pageSize = financeInfoPageInfo.getPageSize();
      Map<String,Object> map = new HashMap<>();
      map.put("list",financeInfos);
      map.put("total",total);
      map.put("pageNum",pageNum);
      map.put("pages",pages);
      map.put("pageSize",pageSize);
      return ResponseResult.SUCCESS("查询成功").setData(map);

  }




}
