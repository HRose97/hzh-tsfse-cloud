package com.hzh.team.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.TeamInfo;

/**
 * @author Hou Zhonghu
 * @since 2022/8/15 16:53
 */
public interface TeamInfoService {

    IPage<TeamInfo> findAllByPage(Page<TeamInfo> page);
}
