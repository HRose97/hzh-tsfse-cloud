package com.hzh.user.controller;

import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:50
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class HzhUserControllrt {

    @Resource
    private HzhUserService hzhUserService;




}
