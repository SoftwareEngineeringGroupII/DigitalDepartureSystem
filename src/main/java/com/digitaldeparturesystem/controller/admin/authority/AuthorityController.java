package com.digitaldeparturesystem.controller.admin.authority;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/authority")
public class AuthorityController {

    @Autowired
    private IAuthorityService authorityService;

    /**
     * 添加权限
     * @param authorities
     * @return
     */
    @PostMapping("/add")
    public ResponseResult addAuthority(@RequestBody Authorities authorities){
        return authorityService.addAuthority(authorities);
    }

}
