package com.hzh.event.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzh.common.pojo.GlobalLocation;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
public interface GlobalLocationService extends IService<GlobalLocation> {

    IPage<GlobalLocation> selectPage(Page<GlobalLocation> page);

    int insert(GlobalLocation globalLocationInsert);

    GlobalLocation selectById(int glId);

    List<GlobalLocation> selectList(QueryWrapper queryWrapper);
}
