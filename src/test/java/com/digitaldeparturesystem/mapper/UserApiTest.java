package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.service.ISectorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApiTest {

    @Resource
    private ISectorService sectorService;



}
