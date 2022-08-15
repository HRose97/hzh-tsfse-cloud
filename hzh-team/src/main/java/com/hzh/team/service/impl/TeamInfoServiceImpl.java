package com.hzh.team.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.mapper.TeamInfoMapper;
import com.hzh.common.pojo.TeamInfo;
import com.hzh.team.service.TeamInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/8/15 16:53
 */
@Service
@Slf4j
public class TeamInfoServiceImpl implements TeamInfoService {

    @Resource
    private TeamInfoMapper teamInfoMapper;

    @Override
    public IPage<TeamInfo> findAllByPage(Page<TeamInfo> page) {
        Page<TeamInfo> teamInfoPage = teamInfoMapper.selectPage(page, null);
        return teamInfoPage;
    }
}
