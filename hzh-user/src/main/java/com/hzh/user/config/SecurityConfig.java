package com.hzh.user.config;

import com.hzh.user.handler.MyAccessDeniedHandler;
import com.hzh.user.handler.MyAuthenticationFailureHandler;
import com.hzh.user.handler.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Security配置类
 *
 * @author Hou Zhonghu
 * @since 2022/7/27 10:15
 */
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;


    @Override
    protected  void configure(HttpSecurity http) throws Exception{
        //formLogin()表示表单提交
        http.formLogin()
                //让所有请求都通过
                .permitAll();
/*
                //自定义登录页面
                .loginPage("/login.html")
                //用户名和密码 正确后  页面的跳转  必须和表单提交的接口一致 也就是登陆页面中的方法名
                //接口名login  登录页面中的接口名
                .loginProcessingUrl("/login")
                //自定义登录成功处理器
                .successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                //自定义登录失败处理器
                .failureHandler(new MyAuthenticationFailureHandler("/error.html"));

                */


        //授权   anyRequest 是有顺序的 从上往下执行  anyRequest放最后面
        http.authorizeRequests()
                .anyRequest().permitAll();

/*
                //放行登录失败后跳转的页面   放行/error.html  不需要认证
                .antMatchers("/error.html").permitAll()
                //如果static下有很多静态文件 比如css和js文件夹  下面则表示放行css和js下所有的目录都会被放行
                //.antMatchers("/css/**","/js/**").permitAll()
                //放行/login.html  不需要认证
                .antMatchers("/login.html").permitAll()
                //所有的请求都必须认证，才能登录   必须登录
                .anyRequest().authenticated();

               */

        //异常处理
        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        //关闭csrf
        http.csrf().disable();
    }


    @Bean
    public PasswordEncoder getPw(){
        return new BCryptPasswordEncoder();
    }


}
