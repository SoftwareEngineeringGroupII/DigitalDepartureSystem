package com.digitaldeparturesystem.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.digitaldeparturesystem.mapper.EduMapper;
import com.digitaldeparturesystem.mapper.LibraryMapper;
import com.digitaldeparturesystem.pojo.FinanceInfo;
import com.digitaldeparturesystem.pojo.LibInfo;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILibraryService;
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
@Service  //图书馆
@Transactional
public class LibraryServiceImpl implements ILibraryService {

     @Resource
     private LibraryMapper libraryMapper;

    @Resource
    private EduMapper eduMapper;

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
        int i = libraryMapper.checkLibrary(stuNumber, bookId);
        //如果确认归还成功
        if (i > 0){
            //此本书审核成功后
            //如果查询该学生需要还书的数量为0,那么将此学生的图书馆状态设置为1
            if (libraryMapper.needReturn(stuNumber)==0) {
                //这里加条件,并且已经上传自己的毕业论文
                if ("1".equals(libraryMapper.findPaper(stuNumber))) {
                    libraryMapper.changeStatus(stuNumber);
                    //审核成功后设置离校流程表libStatus
                     eduMapper.setLibStatus(stuNumber);
                }
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











}
