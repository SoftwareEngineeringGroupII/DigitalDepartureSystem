package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFinanceService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
     * @param stuNum
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForFinance(String stuNum) {
        Map<String, Object> stuInfoFromFinance = financeMapper.getStudentByIdForFinance(stuNum);
        if (stuInfoFromFinance.isEmpty()) {
            return ResponseResult.FAILED("查找失败！没有该学号学生的财务信息");
        }
        return ResponseResult.SUCCESS("查找成功！").setData(stuInfoFromFinance);
    }



    /**
     *  按条件分页查询 ---- 2
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
        List<Map<String, Object>> students = financeMapper.listStudentFinanceInfos(params);
        PageInfo<Map<String, Object>> financePageInfo = new PageInfo<>(students);
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
     * 财务处审核
     * @param stuId
     * @return
     */
    @Override
    public ResponseResult doCheckForFinance(String stuId) {
        try {
            financeMapper.doCheckForFinance(stuId);
            return ResponseResult.SUCCESS("审核成功");
        }catch (Exception e){
            return ResponseResult.FAILED("审核失败,请重新进行操作");
        }
    }




}
