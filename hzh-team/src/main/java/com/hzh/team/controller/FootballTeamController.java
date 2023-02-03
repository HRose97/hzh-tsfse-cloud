package com.hzh.team.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.FootballTeamInfo;
import com.hzh.common.respone.Result;
import com.hzh.team.service.FootballTeamInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 球队
 *
 * @author Hou Zhonghu
 * @since 2022/8/15 16:50
 */
@Slf4j
@RestController
@RequestMapping("/team")
public class FootballTeamController {

    @Resource
    private FootballTeamInfoService footballTeamInfoService;

    //分页查询
    @GetMapping("/football/getAllbastballByPage")
    public Result getAllUserByPage(@RequestParam("current")String current, @RequestParam("size")String size){
        if ( !StringUtils.isEmpty(current) && !StringUtils.isEmpty(current) ){
            Page<FootballTeamInfo> page = new Page<>(Long.parseLong(current), Long.parseLong(size));
            IPage<FootballTeamInfo> footballTeamInfoIPage = footballTeamInfoService.findAllByPage(page);
            return Result.SUCCESS("查询成功",footballTeamInfoIPage);
        }else {
            return Result.FAILED("查询失败");
        }
    }
}
