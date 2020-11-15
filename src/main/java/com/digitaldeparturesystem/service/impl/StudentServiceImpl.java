package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IStudentService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult initManagerAccount(Student student, HttpServletRequest request) {
        //检查是否在初始化

        //检查数据
        if (TextUtils.isEmpty(student.getStuName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(student.getStuPwd())){
            return ResponseResult.FAILED("密码不能为空");
        }
        if (TextUtils.isEmpty(student.getStuNumber())){
            return ResponseResult.FAILED("学号不能为空");
        }
        if (TextUtils.isEmpty(student.getStuDept())){
            return ResponseResult.FAILED("学院不能为空");
        }

        //补充数据
        student.setStuId(String.valueOf(idWorker.nextId()));
//        student.setStuDept();
        return null;
    }
}
