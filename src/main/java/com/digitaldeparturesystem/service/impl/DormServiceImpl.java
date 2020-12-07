package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.DormMapper;
import com.digitaldeparturesystem.pojo.DormInfo;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
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
@Service  //后勤处
@Transactional
public class DormServiceImpl implements IDormService {

    @Resource
    private DormMapper dormMapper;

    /**
     *  上传公告
     * @param notice
     * @param photo
     * @return
     */
    @Override
    public ResponseResult uploadNotice(Notice notice, MultipartFile photo) {
        return null;
    }

    /**
     *  获取学生退寝详情
     * @param studentId
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForFinance(String studentId) {
        Map<String, Object> studentByIdForDorm = dormMapper.getStudentByIdForDorm(studentId);
        if (studentByIdForDorm.isEmpty()) {
            return ResponseResult.FAILED("查找失败,没有该学生的财务信息");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("detail",studentByIdForDorm);

        return ResponseResult.SUCCESS("查找成功").setData(map);
    }


    /**
     * 审核后勤处信息
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult doCheckForDorm(String stuNumber) {
        if (stuNumber == null) {
            return ResponseResult.FAILED("输入学号为空,请重新输入");
        }
        try{
            dormMapper.doCheckForDorm(stuNumber);
            return ResponseResult.SUCCESS("审核成功");
        }catch (Exception e){
            return ResponseResult.FAILED("审核失败,请重新进行操作");
        }
    }

    ///-------///

    /**
     * 按条件分页查询
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param isLeave
     * @return
     */
    public ResponseResult findAllByPageAndType(Integer start, Integer size,
                                               String stuDept, String stuType, String isLeave) {

        //判断类型,如果为所有类型都置空
        stuDept = (stuDept.equals("所有学院")?"":stuDept);
        stuType = (stuType.equals("所有学生")?"":stuType);
        isLeave = (isLeave.equals("所有状态")?"":isLeave);

        //将请求参数放入param中
        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("isLeave",isLeave);
        log.info("param ---->>> "+params);

        //如果未设置显示条数，默认为5
        if (size == 0) {
            size = 5;
        }

        //PageHelper处理分页
        //显示第start也的size条数据
        PageHelper.startPage(start,size);
        List<Map<String, Object>> students = dormMapper.listStudentDormInfos(params);
        if (students.isEmpty()) {
            return ResponseResult.FAILED("没有相关寝室数据");
        }
        PageInfo<Map<String, Object>> dormPageInfo = new PageInfo<>(students);
        int pageNum = dormPageInfo.getPageNum();
        int pages = dormPageInfo.getPages();
        long total = dormPageInfo.getTotal();//获取记录总数

        HashMap<Object, Object> map = new HashMap<>();
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
    public void exportAllDorm(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有信息
        List<DormInfo> dormInfos = dormMapper.listAllDorm();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),DormInfo.class,dormInfos);

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





}