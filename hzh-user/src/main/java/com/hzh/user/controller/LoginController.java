package com.hzh.user.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录
 *
 * @author Hou Zhonghu
 * @since 2022/7/27 9:15
 *
 */

@RestController
public class LoginController {

    @Resource
    private CaptchaService captchaService;

    public  static final String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";



/*    *//**
     * 登录
     *
     * @return
     *//*
    @RequestMapping("/login")
    public String login(){
        return "redirect:main.html";
    }*/


    /**
     * 登录成功跳转
     *
     * @return
     */
    @RequestMapping("/toMain")
    public String main(){
        return "redirect:main.html";
    }

    /**
     * 登录失败跳转
     *
     * @return
     */
    @RequestMapping("/toError")
    public String error(){
        return "redirect:error.html";
    }



    //=============================== 以上为spring security

    @PostMapping("/login")
    public ResponseModel get(@RequestParam("captchaVerification") String captchaVerification) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        //手动判断验证码是否正确
        String codekey = String.format(REDIS_SECOND_CAPTCHA_KEY,captchaVO.getCaptchaVerification());
        CaptchaCacheService local = CaptchaServiceFactory.getCache("local");
        Boolean exists = local.exists(codekey);
        if (exists){
            //存在
            //就可发验证码

        }else {
            //不存在

        }

        ResponseModel response = captchaService.verification(captchaVO);
        String repCode = response.getRepCode();
        System.out.println("response ==> " + repCode);
        if(response.isSuccess() == false){
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员
        }
        return response;
    }

}
