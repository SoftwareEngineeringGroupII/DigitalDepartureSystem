package com.digitaldeparturesystem.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Resource
    public RoleMapper roleMapper;

    @Test
    public void curTest() {
        System.out.println(roleMapper.getRoles().get(0).getName());
    }

}