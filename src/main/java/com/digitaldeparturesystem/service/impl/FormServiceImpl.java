package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Form;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IFormService;
import com.digitaldeparturesystem.service.IMessageService;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.digitaldeparturesystem.utils.TextUtils.isEmpty;

@Service
@Transactional
public class FormServiceImpl implements IFormService {
    /**
     * 离校表单显示
     */
    @Autowired
    private IdWorker idWorker;
    @Override
    public ResponseResult showForm(Form form){
        return null;
    }
}
