package com.hzh.team.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.mapper.FootballTeamInfoMapper;
import com.hzh.common.pojo.BasketballTeamInfo;
import com.hzh.common.pojo.FootballTeamInfo;
import com.hzh.team.service.FootballTeamInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/8/29 15:44
 */
@Service
public class FootballTeamInfoServiceImpl implements FootballTeamInfoService {

    @Resource
    private FootballTeamInfoMapper footballTeamInfoMapper;

    @Override
    public IPage<FootballTeamInfo> findAllByPage(Page<FootballTeamInfo> page) {
        Page<FootballTeamInfo> footballTeamInfoPage = footballTeamInfoMapper.selectPage(page, null);
        return footballTeamInfoPage;
    }


}
