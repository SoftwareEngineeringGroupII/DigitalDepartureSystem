package com.digitaldeparturesystem.controller.admin;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/authority")
public class AuthorityApi {

    @Autowired
    private IAuthorityService authorityService;

    /**
     * 添加权限
     * @param authorities
     * @return
     */
    @PostMapping()
    public ResponseResult addAuthority(@RequestBody Authorities authorities){
        return authorityService.addAuthority(authorities);
    }

    /**
     * 修改权限
     */
    @PutMapping("/{authorityId}")
    public ResponseResult updateAuthority(@PathVariable("authorityId")String authorityId,@RequestBody Authorities authorities){
        return authorityService.updateAuthority(authorityId,authorities);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{authorityId}")
    public ResponseResult deleteAuthority(@PathVariable("authorityId")String authorityId){
        return authorityService.deleteAuthority(authorityId);
    }

    /**
     * 获取所有权限
     */
    @GetMapping()
    public ResponseResult getAllAuthorities(){
        return authorityService.getAllAuthorities();
    }

    /**
     * 得到单个权限
     * @param authorityId
     * @return
     */
    @GetMapping("/{authorityId}")
    public ResponseResult getAuthorityById(@PathVariable("authorityId")String authorityId){
        return null;
    }
}
