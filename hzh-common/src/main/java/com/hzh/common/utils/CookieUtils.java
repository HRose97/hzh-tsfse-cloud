package com.hzh.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 *
 * @author Hou Zhonghu
 * @since 2022/8/3 22:36
 */
@Slf4j
public class CookieUtils {

    //1年
    public static  final int default_age = 60 * 60 * 24 * 365;

    //顶级域名  单点登录使用
    public static final String domain = "Hzh-TSFSE-V1.0";

     /**
      * @Author Hou zhonghu
      * @Description  设置Cookie值
      * @Date 2022/8/3 22:39
      * @Param
      * @return
      **/
     public static void  setUpCookie(HttpServletResponse response,String key,String value){
         setUpCookie(response,key,value,default_age);
     }

    private static void setUpCookie(HttpServletResponse response, String key, String value, int age) {
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        //域名：如果是单点登录，设置顶级域名
        //不设置 就是默认的localhost
        //cookie.setDomain(domain);
        cookie.setMaxAge(age);
        response.addCookie(cookie);

     }

    /**
     * 删除cookie
     *
     */
    public static void  deleteCookie(HttpServletResponse response,String key){
        setUpCookie(response,key,null,0);
    }

    /**
     * 获取cookie
     *
     */
    public static String  getCookie(HttpServletRequest request, String key){
       Cookie[] cookies = request.getCookies();
       if (cookies == null){
           log.info("cookes is null ...");
           return null;
       }
       for (Cookie cookie : cookies){
           if (key.equals(cookie.getName())){
               return cookie.getValue();
           }
       }
       return null;

    }


}
