package com.hzh.team.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.BasketballTeamInfo;
import com.hzh.common.pojo.FootballTeamInfo;

/**
 * @author Hou Zhonghu
 * @since 2022/8/29 15:42
 */
public interface FootballTeamInfoService {



    IPage<FootballTeamInfo> findAllByPage(Page<FootballTeamInfo> page);

}
