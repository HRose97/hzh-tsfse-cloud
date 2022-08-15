package com.hzh.team.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.mapper.CoachMapper;
import com.hzh.common.pojo.CoachInfo;
import com.hzh.common.pojo.HzhUser;
import com.hzh.team.feign.event.DataExtraction;
import com.hzh.team.service.CoachService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/7/6 16:13
 */
@Service
public class CoachServiceImpl implements CoachService {


    @Resource
    private CoachMapper coachMapper;

    @Override
    public IPage<CoachInfo> selectPage(Page<CoachInfo> page) {
        return coachMapper.selectPage(page,null);
    }

    @Override
    public IPage<CoachInfo> findAllByPage(Page<CoachInfo> page) {
        Page<CoachInfo> coachInfoPage = coachMapper.selectPage(page, null);
        return coachInfoPage;
    }


}
