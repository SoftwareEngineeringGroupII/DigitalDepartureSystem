package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;



public interface IAdminService {

    ResponseResult initManagerAccount(Clerk clerk);

    ResponseResult findAuditMenu();
}
