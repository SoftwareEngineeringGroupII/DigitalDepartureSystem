package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.mapper.LibraryMapper;
import com.digitaldeparturesystem.pojo.*;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILibraryService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.internal.$Gson$Preconditions;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service  //图书馆
@Transactional
public class LibraryServiceImpl implements ILibraryService {

     @Resource
     private LibraryMapper libraryMapper;

    @Resource
    private EduMapper eduMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 分页查询所有
     * @return
     */
    public ResponseResult findAllByPage(Integer start,Integer size){
        if (size == 0){
            size = 5;
        }

        PageHelper.startPage(start,size);
        List<LibInfo> libInfos = libraryMapper.listAllLibrary();
        if (libInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        PageInfo<LibInfo> libInfoPageInfo = new PageInfo<>(libInfos);
        long total = libInfoPageInfo.getTotal();//总记录数
        int pageNum = libInfoPageInfo.getPageNum();//当前页
        int pages = libInfoPageInfo.getPages();//总页数
        int pageSize = libInfoPageInfo.getPageSize();
        Map<String,Object> map = new HashMap<>();
        map.put("list",libInfos);
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pages",pages);//总页数
        map.put("pageSize",pageSize);

        return ResponseResult.SUCCESS("查询成功").setData(map);
    }


    /**
     *  查询某个学生的借书信息
     * @param stuNumber
     * @return
     */
    public ResponseResult getStuBookDetail(String stuNumber){
        //先查询是否有这个学生存在
        Student stuByStuNumber = libraryMapper.findStuByStuNumber(stuNumber);
        if (stuByStuNumber == null) {
            return ResponseResult.FAILED("查询失败,没有这个学生存在！");
        }
        //查询学生图书馆基本信息(学号-姓名-学院-学生类型-未还书总数-图书馆通过状态)
        LibInfo stuLibrary = libraryMapper.findStuLibrary(stuNumber);
        //查询该学生借书列表(书编号-书名-归还状态)
        List<Map<String, Object>> list = libraryMapper.bookDetail(stuNumber);
        if (list.isEmpty()) {
           return ResponseResult.FAILED("没有该学生的借书信息");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("stuInfo",stuLibrary);
        map.put("list",list);
        return ResponseResult.SUCCESS("查询成功").setData(map);
    }


    /**
     *  根据学号和图书ID确认归还状态
     * @param stuNumber
     * @param bookId
     * @return
     */
    public ResponseResult checkLibrary(String stuNumber,String bookId){
        //根据学号和图书id审核某一本书
        libraryMapper.checkLibrary(stuNumber, bookId);
        //如果确认归还成功
        //此本书审核成功后
        //如果查询该学生需要还书的数量为0,将此学生的图书馆状态设置为1
        if (libraryMapper.needReturn(stuNumber)==0) {
                //这里加条件,并且已经上传自己的毕业论文
            if ("1".equals(libraryMapper.findPaper(stuNumber))) {
                    //修改图书馆的总审核状态
                libraryMapper.changeStatus(stuNumber);
                    //审核成功后设置离校流程表libStatus
                eduMapper.setLibStatus(stuNumber);
                    //向学生端发送审核最新信息
                sendMessageForLib(stuNumber);
            }
              //返回审核成功
             return ResponseResult.SUCCESS("审核成功！");
        }
        return ResponseResult.FAILED("审核失败！请重新操作");
    }



    /**
     *  导出所有财务信息
     * @param response
     */
    public void exportAllLib(HttpServletResponse response) throws UnsupportedEncodingException {
        //查询数据库中所有信息
        List<LibInfo> libInfos = libraryMapper.listAllLibrary();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),LibInfo.class,libInfos);

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("图书馆借书情况表","UTF-8")+".xls");
        response.setCharacterEncoding("UTF-8");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 按条件分页查询
     * @param start
     * @param size
     * @param stuDept
     * @param stuType
     * @param libStatus
     * @return
     */
    public ResponseResult findAllByPageAndType(Integer start,Integer size,
                                               String stuDept,String stuType,String libStatus){

        //判断类型,如果是所有类型的状态将其置空
        stuDept = (stuDept.equals("所有学院") ?"":stuDept);
        stuType =(stuType.equals("所有学生")?"":stuType);
        libStatus = (libStatus.equals("所有状态")?"":libStatus);

        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("libStatus",libStatus);
        log.info("params --- >> "+params);

        if(size==0){
            //如果未设置显示条数，默认为5
            size=5;
        }

        //pageHelper使用
        //分页处理,显示第start页的size条数据
        PageHelper.startPage(start,size);
        //List<FinanceInfo> students = financeMapper.listStudentFinanceInfos(params);
        List<LibInfo> libInfos = libraryMapper.listAllLibrary();
        if (libInfos.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        PageInfo<LibInfo> libInfoPageInfo = new PageInfo<>(libInfos);
        int pageNum = libInfoPageInfo.getPageNum();
        int pages =libInfoPageInfo.getPages();
        long total = libInfoPageInfo.getTotal();//获取记录总数

      /*  for (Map<String, Object> student : students) {
            System.out.println(student);
        } */

        Map<String,Object> map = new HashMap<>();
        map.put("list",libInfos);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);
        return ResponseResult.SUCCESS("查询成功").setData(map);

    }


    /**
     * 检书,设置其需要缴费
     * @param degree 损坏程度
     * @param bookId
     * @return
     */
    public void updateBookPay(String degree,String bookId){
        double zhe;
        if ("未损坏".equals(degree)) {
            zhe=0;
            libraryMapper.updateBookPay(zhe,bookId);
        }
        if ("一般损坏".equals(degree)){
            zhe=0.2;
            libraryMapper.updateBookPay(zhe,bookId);
        }
        if ("严重损坏".equals(degree)) {
            zhe=0.7;
            libraryMapper.updateBookPay(zhe,bookId);
        }
        if ("图书丢失".equals(degree)){
            zhe=1;
            libraryMapper.updateBookPay(zhe,bookId);
        }
        //return ResponseResult.SUCCESS("设置成功");
    }



    /**
     * 汇总总计罚款图书以及需缴费
     * @return
     */
    public ResponseResult sumPayForLib(String stuNumber){
        List<Book> books = libraryMapper.sumPayBook(stuNumber);//所有书
        Double sumPay = libraryMapper.sumPayForLib(stuNumber);//总计多少钱
        Map<String,Object> map = new HashMap<>();
        map.put("list",books);
        map.put("total",sumPay);
        return ResponseResult.SUCCESS("查询成功").setData(map);
    }



    /**
     * 图书馆审核完后向学生发送消息 == 图书馆审核详情欠费详情
     * @param stuNumber
     * @return
     */
    public void sendMessageForLib(String stuNumber){
        Message message = new Message();
        List<Book> books = libraryMapper.sumPayBook(stuNumber);
        StringBuilder details = new StringBuilder();
        for (Book book : books) {
            String bookName = book.getBookName();
            double pay = book.getPay();
            details.append(" 书名：").append(bookName).append(" 应缴费：").append(pay).append("\n");
        }
        Double sumPayForLib = libraryMapper.sumPayForLib(stuNumber);
        //如果没有要缴费的书
        if (books.isEmpty()){
            //向学生端发送一个审核通过信息
            message.setContent("图书馆审核已通过..");
        }else {
            message.setContent(details.toString()+'\n'+"总计："+sumPayForLib+'\n'+"请到财务处进行缴费"+'\n');
        }
        message.setTitle("图书管审核通知");
        message.setMessageID(idWorker.nextId()+"");
        message.setMsgStatus("2");//默认为1,学生端查看
        message.setSendID(libraryMapper.findStuIDByNumber(stuNumber));
        message.setReceiveID("图书馆");
        message.setMessagedate(new Date());
        //保存到数据库
        libraryMapper.sendMessageByLib(message);
    }


    /**
     * 图书馆审核书 == 新
     * @param stuNumber
     * @param bookId
     * @param degree
     * @return
     */
    public ResponseResult checkLibStatus(String stuNumber,String bookId,String degree){
          //审核者一本书损坏程度,该如何计算罚款==
          updateBookPay(degree,bookId);
          libraryMapper.checkLibrary(stuNumber,bookId);//修改该本书状态==1
          //这本书审核完后查询该学生还书数量为0,换清书
        if (libraryMapper.needReturn(stuNumber)==0) {
            //并且已经上传自己的论文
            if ("1".equals(libraryMapper.findPaper(stuNumber))) {
                //修改图书馆的总审核状态
                libraryMapper.changeStatus(stuNumber);
                //审核成功后设置离校流程表libStatus
                eduMapper.setLibStatus(stuNumber);
                //将应缴费设置到财务处
                libraryMapper.updateFinanceBook(stuNumber);
                //更新finance中的expense
                sumExpense(stuNumber);
                //向学生端发送审核最新信息
                sendMessageForLib(stuNumber);
                return ResponseResult.SUCCESS("图书馆审核通过");
            }else {
                return ResponseResult.FAILED("还未上传论文,不可以通过");
            }
        }
        return ResponseResult.SUCCESS("该本书通过检查审核");
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
     * 弹出某本书详情框
     * @return
     */
    public ResponseResult detailForBook(String bookID){
        Map<String, Object> map = libraryMapper.detailBook(bookID);
        List<Map<String,Object>> details = new ArrayList<>();
        details.add(map);
        return ResponseResult.SUCCESS("查看这本详情").setData(details);
    }











}
