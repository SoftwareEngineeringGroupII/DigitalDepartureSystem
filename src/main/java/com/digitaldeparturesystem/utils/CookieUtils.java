package com.digitaldeparturesystem.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    //一年
    private static final int default_age = 60 * 60 * 34 * 365;

    //TODO:修改服务器的ip
    private static final String domain = "localhost";
//    private static final String domain = "192.168.172.131";


    /**
     * 设置cookie值
     * @param response
     * @param key
     * @param value
     */
    public static void setUpCookie(HttpServletResponse response, String key, String value) {
        setUpCookie(response, key, value, default_age);
    }

    public static void setUpCookie(HttpServletResponse response, String key, String value, int age) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        //不设置cookies的时间，关闭浏览器，自动清除
        cookie.setMaxAge(age);
//        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     * @param response
     * @param key
     */
    public static void deleteCookie(HttpServletResponse response, String key){
        setUpCookie(response,key,null,0);
    }

    /**
     * 获取cookie
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
