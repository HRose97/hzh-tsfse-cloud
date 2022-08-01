package com.hzh.user.handler;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 登录成功处理器
 *
 * @author Hou Zhonghu
 * @since 2022/7/27 16:21
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private String url;

    public MyAuthenticationSuccessHandler(String url){
        this.url = url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getUsername());
        //密码基于安全考虑是null
        System.out.println(user.getPassword());
        System.out.println(user.getAuthorities());
        httpServletResponse.sendRedirect(url);
    }
}
