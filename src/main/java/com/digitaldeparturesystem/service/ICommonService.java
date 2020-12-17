package com.digitaldeparturesystem.service;

import com.digitaldeparturesystem.pojo.PasswordBean;
import com.digitaldeparturesystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICommonService {

    /**
     * 得到当前用户所拥有的菜单栏权限
     * @return
     */
    ResponseResult getMenu();

    /**
     * 获取当前用户的信息
     * @return
     */
    ResponseResult getUserInfo();

    /**
     * 创建验证码
     * @param response
     * @param captchaKey
     * @throws Exception
     */
    void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception;


    /**
     * 发送邮箱验证码
     * @param request
     * @param emailAddress
     * @return
     */
    ResponseResult sendEmail(HttpServletRequest request, String emailAddress);


    /**
     * 更改密码
     * @param verifyCode
     * @return
     */
    ResponseResult recoveredUserPassword(String verifyCode, String password,String userAccount);

    /**
     * 更新用户密码
     * @param captcha
     * @param captchaKey
     * @param passwordBean
     * @return
     */
    ResponseResult updateUserPassword(String captcha, String captchaKey, PasswordBean passwordBean);

    ResponseResult updateUserPassword(PasswordBean passwordBean);
}
