package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IDormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service  //后勤处
@Transactional
public class DormServiceImpl implements IDormService {

    @Override
    public ResponseResult uploadNotice(Notice notice, MultipartFile photo) {
        return null;
    }

    @Override
    public ResponseResult getStudentByIdForFinance(String studentId) {
        return null;
    }

    @Override
    public ResponseResult findAllByPageAndType(Integer start, Integer size, String stuDept, String stuType, String financeStatus) {
        return null;
    }

    @Override
    public ResponseResult doCheckForDorm(String stuNumber) {
        return null;
    }
}
