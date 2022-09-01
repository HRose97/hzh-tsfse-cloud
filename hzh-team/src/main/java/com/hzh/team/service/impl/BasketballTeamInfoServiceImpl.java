package com.hzh.team.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.mapper.BasketballTeamInfoMapper;
import com.hzh.common.pojo.BasketballTeamInfo;
import com.hzh.team.service.BasketballTeamInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/8/15 16:53
 */
@Service
@Slf4j
public class BasketballTeamInfoServiceImpl implements BasketballTeamInfoService {

    @Resource
    private BasketballTeamInfoMapper basketballTeamInfoMapper;

    @Override
    public IPage<BasketballTeamInfo> findAllByPage(Page<BasketballTeamInfo> page) {
        Page<BasketballTeamInfo> teamInfoPage = basketballTeamInfoMapper.selectPage(page, null);
        return teamInfoPage;
    }
}
