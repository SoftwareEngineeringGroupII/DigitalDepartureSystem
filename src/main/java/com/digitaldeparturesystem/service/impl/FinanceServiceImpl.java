package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.FinanceMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFinanceService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
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
     *  财务处按条件分页查询
     * @param map
     * @return
     */
    @Override
    public ResponseResult findAllByPageAndType(Map<String, Object> map) {
        Integer page = (Integer) map.get("page");
        Integer size = (Integer) map.get("size");
        String stuDept = (String) map.get("stuDept");
        String stuType = (String) map.get("stuType");
        String financeStatus = (String) map.get("financeStatus");

        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("financeStatus",financeStatus);
        log.info("params --- >> "+params);

        //pageHelper使用
        PageHelper.startPage(page,size);
        List<Map<String, Object>> students = financeMapper.listStudentFinanceInfos(params);
        PageInfo<Map<String, Object>> financePageInfo = new PageInfo<>(students);
        int pageNum = financePageInfo.getPageNum();
        int pages = financePageInfo.getPages();
        if (students.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }
        Map<String,Object> map1 = new HashMap<>();
        map1.put("list",students);
        map1.put("pageNum",pageNum);
        map1.put("pages",pages);
        return ResponseResult.SUCCESS("查询成功").setData(map1);

    }

    /**
     * 财务处审核
     * @param stuNumber
     * @return
     */
    @Override
    public ResponseResult doCheckForFinance(String stuNumber) {
        return null;
    }


}
