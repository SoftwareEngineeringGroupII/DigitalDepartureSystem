package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.DetailMapper;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDetailsService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class DetailsServiceImpl implements IDetailsService {
    @Resource
    private DetailMapper detailMapper;
    /**
     * 学生信息显示
     * @param stuId
     * @return
     */

    @Override
    public ResponseResult showStuDetailsById(String stuId) {
        Map<String,Object> studetails = detailMapper.showDetailsById(stuId);
        if (studetails == null){
            return ResponseResult.FAILED("查看个人信息失败！");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("detail",showStuDetailsById(stuId));
        return ResponseResult.SUCCESS("查找成功").setData(map);
    }
}
