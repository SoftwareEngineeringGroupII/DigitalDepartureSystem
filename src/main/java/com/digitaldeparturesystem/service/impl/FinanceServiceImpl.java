package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.pojo.FinanceInfo;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFinanceService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service  //一卡通
@Transactional
public class FinanceServiceImpl implements IFinanceService {

    @Autowired
    private IdWorker idWorker;

    @Resource
    private FinanceMapper financeMapper;


    public ResponseResult uploadNotice(Notice notice, MultipartFile photo){

        return null;
    }



    /**
     *  根据学号获取学生财务缴费情况
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForFinance(String stuNumber) {
        if (stuNumber == null) {
            return ResponseResult.FAILED("输入学号为空,请重新输入");
        }
        FinanceInfo stuInfoFromFinance = financeMapper.getStudentByIdForFinance(stuNumber);
        if (stuInfoFromFinance==null) {
            return ResponseResult.FAILED("查找失败！没有该学号学生的财务信息");
        }
        return ResponseResult.SUCCESS("查找成功！").setData(stuInfoFromFinance);
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
       // List<Map<String, Object>> students = financeMapper.listStudentFinanceInfos(params);
        List<FinanceInfo> students = financeMapper.listStudentFinanceInfos(params);
        //PageInfo<Map<String, Object>> financePageInfo = new PageInfo<>(students);
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
     * 财务处审核 根据学生学号：修改审核状态
     * @param stuNum
     * @return
     */
    @Override
    public ResponseResult doCheckForFinance(String stuNum) {
        try {
            financeMapper.doCheckForFinance(stuNum);
            return ResponseResult.SUCCESS("审核成功");
        }catch (Exception e){
            return ResponseResult.FAILED("审核失败,请重新进行操作");
        }
    }


    /**
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
      map.put("total",total);
      map.put("pageNum",pageNum);
      map.put("pages",pages);
      map.put("pageSize",pageSize);
      return ResponseResult.SUCCESS("查询成功").setData(map);

  }





}
