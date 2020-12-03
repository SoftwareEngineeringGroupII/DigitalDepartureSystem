package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.response.ResponseResult;

public interface ICommonService {

    /**
     * 得到当前用户所拥有的菜单栏权限
     * @return
     */
    ResponseResult getMenu();

}
