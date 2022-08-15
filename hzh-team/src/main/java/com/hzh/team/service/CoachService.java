package com.hzh.team.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.CoachInfo;

/**
 * @author Hou Zhonghu
 * @since 2022/7/6 16:13
 */
public interface CoachService {

   IPage<CoachInfo> selectPage(Page<CoachInfo> page);

    IPage<CoachInfo> findAllByPage(Page<CoachInfo> page);
}
