package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Form;
import com.digitaldeparturesystem.response.ResponseResult;

public interface IFormService {
    /**
     * 学生信息表单
     * @return
     */
    ResponseResult showFormByStudent();

    /**
     * 学生进程表单
     * @return
     */
    ResponseResult showFormByProcess();
}
