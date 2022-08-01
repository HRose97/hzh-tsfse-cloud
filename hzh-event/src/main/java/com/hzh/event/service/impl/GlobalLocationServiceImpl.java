package com.hzh.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzh.common.mapper.GlobalLocationMapper;
import com.hzh.common.pojo.GlobalLocation;
import com.hzh.event.service.GlobalLocationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Service
public class GlobalLocationServiceImpl extends ServiceImpl<GlobalLocationMapper, GlobalLocation> implements GlobalLocationService {


    @Resource
    private GlobalLocationMapper globalLocationMapper;

    @Override
    public IPage<GlobalLocation> selectPage(Page<GlobalLocation> page) {
        return globalLocationMapper.selectPage(page,null);
    }

    @Override
    public int insert(GlobalLocation globalLocationInsert) {
        int insert = globalLocationMapper.insert(globalLocationInsert);
        return insert;
    }

    @Override
    public GlobalLocation selectById(int glId) {
        GlobalLocation globalLocation = globalLocationMapper.selectById(glId);
        return globalLocation;
    }

    @Override
    public List<GlobalLocation> selectList(QueryWrapper queryWrapper) {
        return globalLocationMapper.selectList(null);
    }
}
