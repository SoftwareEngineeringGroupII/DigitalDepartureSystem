package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.DormMapper;
import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.mapper.LibraryMapper;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDormService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service  //后勤处
@Transactional
public class DormServiceImpl implements IDormService {

    @Resource
    private DormMapper dormMapper;

    @Resource
    private EduMapper eduMapper;

    @Resource
    private LibraryMapper libraryMapper;

    @Autowired
    private IdWorker idWorker;


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
     *  根据学号获取学生退寝详情
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForFinance(String stuNumber) {
        //先查询是否有该学生存在
        Student stuByStuNumber = dormMapper.findStuByStuNumber(stuNumber);
        if (stuByStuNumber == null) {
            return ResponseResult.FAILED("查询失败,没有这个学生存在！");
        }
        //再查询是否有该学生的退寝详情
        DormInfo studentByIdForDorm = dormMapper.getStudentByIdForDorm(stuNumber);
        if (studentByIdForDorm==null) {
            return ResponseResult.FAILED("查找失败,没有该学生的财务信息");
        }

        List<DormInfo> list = new ArrayList<>();
        list.add(studentByIdForDorm);
        return ResponseResult.SUCCESS("查找成功").setData(list);
    }


    /**
     * 审核学生退寝信息
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult doCheckForDorm(String stuNumber) {
            //设置后勤表
            dormMapper.doCheckForDorm(stuNumber);
            //审核成功后设置流程表的dormStatus
            eduMapper.setDormStatus(stuNumber);
            return ResponseResult.SUCCESS("审核成功");
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

    /**
     *  查询所有退寝状态
     * @return
     */
    public ResponseResult selectAll(){
        List<DormInfo> dormInfos = dormMapper.listAllDorm();
        if (dormInfos.isEmpty()) {
            return ResponseResult.FAILED("查询失败");
        }
        return ResponseResult.SUCCESS("查询成功").setData(dormInfos);
    }

    /**
     * 分页查询所有的退寝状态
     * @return
     */
    public ResponseResult selectAllByPage(Integer start,Integer size){
        if (size == null) {
            size = 5;
        }
        PageHelper.startPage(start,size);
        List<DormInfo> dormInfos = dormMapper.listAllDorm();
        if (dormInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        PageInfo<DormInfo> dormInfoPageInfo = new PageInfo<>(dormInfos);
        long total = dormInfoPageInfo.getTotal();
        int pageNum = dormInfoPageInfo.getPageNum();
        int pages = dormInfoPageInfo.getPages();
        int pageSize = dormInfoPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",dormInfos);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("pageSize",pageSize);
        return ResponseResult.SUCCESS("查询成功").setData(map);
    }



    /**
     * 审核后勤处,除了归还钥匙,还有看有没有破坏啥东西赔钱
     * @return
     */
    public ResponseResult checkDormStatus(String stuNumber, DormPay dormPay){
        double total = 0;
        //循环插入数据 == 赔偿明细
        dormPay.setDormpayID(idWorker.nextId()+"");
        dormPay.setStuID(libraryMapper.findStuIDByNumber(stuNumber));
        total=dormPay.getPay();//总计后勤处需要换的钱
        dormMapper.insertDormPay(dormPay);
        //往财务处打钱(注意这里是用传入的新对象去更新财务处的dormFine,覆盖)
        dormMapper.updateFinanceDorm(stuNumber,total);
        //更新finance的总金额
        sumExpense(stuNumber);
        //发送message
        sendMessage(stuNumber,dormPay);
        //修改其后勤处审核状态
        dormMapper.doCheckForDorm(stuNumber);
        //修改process表状态
        eduMapper.setDormStatus(stuNumber);
        //修改
        return ResponseResult.SUCCESS("退寝成功");
    }


    /**
     * 后勤处审核成功后发送学生端消息
     * @param stuNumber
     * @return
     */
    public void sendMessage(String stuNumber, DormPay dormPay){
        Message message = new Message();
        //查询这个人的后勤退款明细(本来也只会调一次接口,防止测试多个结果存入数据造成返回多个数据异常)
      //  List<DormPay> dormPays = dormMapper.findDormPay(stuNumber);
        String detail = dormPay.getDetail();
        double pay = dormPay.getPay();
        StringBuilder details = new StringBuilder();
        details.append("欠款明细：").append(detail).append('\n').append("应缴费：").append(pay).append('\n');
        //查询finance中后勤处应该缴费多少
        //Double sumDormpay = dormMapper.findSumDormpay(stuNumber);
        if (pay > 0){
            message.setContent(details.toString()+'\n'+"后勤处已经审核,请到财务处缴费");
        }else{
            message.setContent("后勤处已审核通过");
        }
        message.setTitle("后勤处审核通知");
        message.setMessageID(idWorker.nextId()+"");
        message.setMsgStatus("2");
        message.setReceiveID("后勤处");
        message.setSendID(libraryMapper.findStuIDByNumber(stuNumber));
        message.setMessagedate(new Date());
        //保存message
        dormMapper.sendMessage(message);
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
     *  弹框显示某学生学生退寝详情
     * @return
     */
    public ResponseResult detailDorm(String stuNumber){
        Map<String, Object> map = dormMapper.detailDorm(stuNumber);
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return ResponseResult.SUCCESS("查询成功").setData(list);
    }









}