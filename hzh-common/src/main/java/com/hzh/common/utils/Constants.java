package com.hzh.common.utils;

/**
 * 常用量工工具类
 *
 * @author Hou Zhonghu
 * @since 2022/8/2 16:22
 */
public interface Constants {


    interface User{
        //禁止/删除
        String FORBIDDENT_STATE = "1";
        //使用/存在
        String UNFORBIDDENT_STATE = "0";
        //黑名单
        String USER_BLACKELIST = "2";
        //普通用户
        String MEMBER_USER="会员";

    }


    interface  UserDescription{
        String PT_USER = "平台管理员";
        String XT_USER = "系统管理员";
        String SS_USER = "赛事管理员";
        String CG_USER = "场馆管理员";
        String QD_USER = "球队管理员";
        String YK_FK_USER = "游客/访客";
        String MEMEBR_USER = "会员";
        String VIP_USER = "贵宾";
    }

    interface  UserType{
        String PT_USER = "check";
        String XT_USER = "sys";
        String SS_USER = "events";
        String CG_USER = "sportstypeman";
        String QD_USER = "teams";
        String YK_FK_USER = "visitor";
        String MEMEBR_USER = "member";
        String VIP_USER = "vip";
    }




}
