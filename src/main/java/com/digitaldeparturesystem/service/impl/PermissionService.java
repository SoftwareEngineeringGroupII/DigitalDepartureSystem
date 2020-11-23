package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.service.ISectorService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.CookieUtils;
import com.digitaldeparturesystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service("permission")
public class PermissionService {

    @Autowired
    private ISectorService sectorService;

    /**
     * 判断是不是管理员
     * @return
     */
    public boolean sector(){
        //拿到request和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String tokenKey = CookieUtils.getCookie(request, Constants.Clerk.COOKIE_TOKEN_KEY);
        //没有令牌的key，没有登录，不往下执行
        if (TextUtils.isEmpty(tokenKey)) {
            return false;
        }
        Clerk clerk = sectorService.checkClerk();
        if (clerk == null) {
            return false;
        }
//        if (Constants.Clerk.POWER_ADMIN.equals((clerk.getClerkPower()))) {
//            //部门成员
//            return true;
//        }
        return false;
    }

}
