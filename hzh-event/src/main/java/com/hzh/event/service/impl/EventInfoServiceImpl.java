package com.hzh.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzh.common.mapper.EventInfoMapper;
import com.hzh.common.pojo.EventInfo;
import com.hzh.event.service.EventInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 体育赛事举办信息表 服务实现类
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Service
public class EventInfoServiceImpl extends ServiceImpl<EventInfoMapper, EventInfo> implements EventInfoService {

    @Resource
    private EventInfoMapper eventInfoMapper;

    @Override
    public int insert(EventInfo eventInfoInsert) {
        int insert = eventInfoMapper.insert(eventInfoInsert);
        return insert;
    }

    @Override
    public int updateById(int physicalId, int physicalStatus) {
        int i = eventInfoMapper.updateById(physicalId, physicalStatus);
        return i;
    }

    @Override
    public EventInfo selectById(int physicalId) {
        EventInfo eventInfo = eventInfoMapper.selectById(physicalId);
        return eventInfo;
    }

    @Override
    public IPage<EventInfo> selectPage(Page<EventInfo> page) {
        return eventInfoMapper.selectPage(page,null);
    }

    @Override
    public List<EventInfo> selectList(QueryWrapper queryWrapper) {
        List<EventInfo> eventInfos = eventInfoMapper.selectList(queryWrapper);
        return eventInfos;
    }

    @Override
    public int getMaxId() {
        int id = eventInfoMapper.getMaxId();
        return id;
    }


}
