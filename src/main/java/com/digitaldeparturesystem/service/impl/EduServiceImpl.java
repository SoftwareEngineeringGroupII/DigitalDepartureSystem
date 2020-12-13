package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.pojo.DormInfo;
import com.digitaldeparturesystem.pojo.StuBasicInfo;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.pojo.Process;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IEduService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service //教务处
@Transactional
public class EduServiceImpl implements IEduService {

    @Resource
    private EduMapper eduMapper;

    /**
     *  导出所有财务信息
     * @param response
     */
    public void exportAllDorm(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有信息
        List<Map<String, Object>> maps = eduMapper.listAllEdu();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),DormInfo.class,maps);

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("后勤处审核表","UTF-8")+".xls");
        response.setCharacterEncoding("UTF-8");
        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    /**
     * 分页查询所有的退寝状态
     * @return
     */
    public ResponseResult selectAllByPage(Integer start, Integer size){
        if (size == null) {
            size = 5;
        }
        PageHelper.startPage(start,size);
        List<Map<String, Object>> maps = eduMapper.listAllEdu();
        if (maps.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(maps);
        long total = mapPageInfo.getTotal();
        int pageNum = mapPageInfo.getPageNum();
        int pages = mapPageInfo.getPages();
        int pageSize = mapPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",maps);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("pageSize",pageSize);
        return ResponseResult.SUCCESS("查询成功").setData(map);
    }


    /**
     * 根据学号查询离校状态
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
        return ResponseResult.SUCCESS("查询成功");
    }

    /**
     *  根据学号查询学生教务处详情
     * @param stuNumber
     * @return
     */
    public ResponseResult findStuDetailForEdu(String stuNumber){
        //查询学校离校流程进度
        Process stuProcess = eduMapper.getStuProcess(stuNumber);
        //查询学生基本信息
        StuBasicInfo stuBasicInfo = eduMapper.getStuBasicInfo(stuNumber);

       // List<Map<String,Object>> list = new ArrayList<>();
       Map<String,Object> map = new HashMap<>();
       map.put("stuInfo",stuBasicInfo);
       map.put("process",stuProcess);

       return ResponseResult.SUCCESS("查询成功").setData(map);

    }





}
