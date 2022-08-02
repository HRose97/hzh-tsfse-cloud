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
    }

}
