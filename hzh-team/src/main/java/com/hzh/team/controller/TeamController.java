package com.hzh.team.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.CoachInfo;
import com.hzh.common.pojo.TeamInfo;
import com.hzh.common.respone.R;
import com.hzh.team.service.TeamInfoService;
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
public class TeamController {

    @Resource
    private TeamInfoService teamInfoService;

    //分页查询
    @GetMapping("/bastball/getAllbastballByPage")
    public R getAllUserByPage(@RequestParam("current")String current, @RequestParam("size")String size){
        if ( !StringUtils.isEmpty(current) && !StringUtils.isEmpty(current) ){
            Page<TeamInfo> page = new Page<>(Long.parseLong(current), Long.parseLong(size));
            IPage<TeamInfo> coachInfo = teamInfoService.findAllByPage(page);
            return R.SUCCESS("查询成功",coachInfo);
        }else {
            return R.FAILED("查询失败");
        }
    }
}
