package com.hzh.order.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/7/5 20:59
 */
@RequestMapping("/hello")
public class TestColler {

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/gethzh")
    public String test(){
        //RedisKeyUtil redisKeyUtil = new RedisKeyUtil();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("H", "测试添加");
        //redisUtil.set("H", "测试添加");
        return "你好！";
    }




}
