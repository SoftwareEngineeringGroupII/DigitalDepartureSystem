package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDetailsService;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class DetailsServiceImpl implements IDetailsService {
    /**
     * 学生信息显示
     * @param student
     * @return
     */
    @Override
    public ResponseResult showStuDetails(Student student) {
        return null;
    }
}
