package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface IAdminService {

    /**
     * 初始化管理账号
     * @param clerk
     * @return
     */
    ResponseResult insertManagerAccount(Clerk clerk);

    /**
     * 查找能展示的菜单
     * @return
     */
    ResponseResult findAuditMenu();

    /**
     * 增加用户
     * @param clerk
     * @return
     */
    ResponseResult insertClerk(Clerk clerk, HttpServletRequest request);

    /**
     * 删除用户
     * @param clerk
     * @return
     */
    ResponseResult deleteClerkByStatus(String clerk);

    /**
     * 更新用户
     * @param clerkId
     * @param clerk
     * @return
     */
    ResponseResult updateClerk(String clerkId, Clerk clerk);

    /**
     * 得到clerk通过id
     * @param clerkId
     * @return
     */
    ResponseResult findClerkById(String clerkId);

    /**
     * 获取全部的clerk
     * @return
     */
    ResponseResult findAllClerks();

    /**
     * 给用户添加角色
     * @return
     */
    ResponseResult insertRoleToUser(String clerkId, List<String> roleIds);

    /**
     * 获取用户所拥有的权限
     * @param clerkId
     * @return
     */
    ResponseResult findRolesByUser(String clerkId);
}
