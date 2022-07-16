package com.hzh.event.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzh.common.pojo.ChinaCity;
import com.hzh.common.pojo.EventInfo;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
public interface ChinaCityService extends IService<ChinaCity> {



    int insert(ChinaCity chinaCityInsert);

    ChinaCity selectById(long id);

    int updateById(long id, String state);

    List<ChinaCity> selectList(QueryWrapper queryWrapper);

    IPage<ChinaCity> selectPage(Page<ChinaCity> page);
}
