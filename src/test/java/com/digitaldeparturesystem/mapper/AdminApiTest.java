package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApiTest {

    @Resource
    private IAdminService adminService;

    @Test
    public void insertDataToUserRole(){
        String clerkId = "78187113611054284111111";
        List<String> list = new ArrayList<>();
        list.add("782284404641759231111111111");
        ResponseResult responseResult = adminService.insertRoleToUser(clerkId, list);
        System.out.println(responseResult);
    }

    @Test
    public void addRoleToUser(){
        String clerkId = "78187113611054284111111";
        List<String> list = new ArrayList<>();
        list.add("782284404641759231111111111");
        ResponseResult responseResult = adminService.deleteRoleToUser(clerkId, list);
        System.out.println(responseResult);
    }
}
