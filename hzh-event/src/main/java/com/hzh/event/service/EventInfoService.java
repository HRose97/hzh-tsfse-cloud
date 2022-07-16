package com.hzh.event.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzh.common.pojo.EventInfo;

import java.util.List;

/**
 * <p>
 * 体育赛事举办信息表 服务类
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
public interface EventInfoService extends IService<EventInfo> {

    int insert(EventInfo eventInfoInsert);

    int updateById(int physicalId, int physicalStatus);

    EventInfo selectById(int physicalId);

    IPage<EventInfo> selectPage(Page<EventInfo> page);

    List<EventInfo> selectList(QueryWrapper queryWrapper);

    int getMaxId();

}
