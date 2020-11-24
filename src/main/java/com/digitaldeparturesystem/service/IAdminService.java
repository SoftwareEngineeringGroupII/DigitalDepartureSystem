package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.response.ResponseResult;



public interface IAdminService {


    ResponseResult initManagerAccount(Clerk clerk);

    ResponseResult findAuditMenu();

    ResponseResult addClerk(Clerk clerk);

    ResponseResult deleteClerk(String clerk);

    ResponseResult updateClerk(String clerkId, Clerk clerk);
}
