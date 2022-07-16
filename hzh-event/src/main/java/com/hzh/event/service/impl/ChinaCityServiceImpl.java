package com.hzh.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzh.common.mapper.ChinaCityMapper;
import com.hzh.common.pojo.ChinaCity;
import com.hzh.common.pojo.EventInfo;
import com.hzh.event.service.ChinaCityService;
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
public class ChinaCityServiceImpl extends ServiceImpl<ChinaCityMapper, ChinaCity> implements ChinaCityService {


    @Resource
    private ChinaCityMapper chinaCityMapper;



    @Override
    public int insert(ChinaCity chinaCityInsert) {
        int insert = chinaCityMapper.insert(chinaCityInsert);
        return insert;
    }

    @Override
    public ChinaCity selectById(long id) {
        ChinaCity chinaCity = chinaCityMapper.selectById(id);
        return chinaCity;
    }

    @Override
    public int updateById(long id, String state) {
        int b = chinaCityMapper.updateById(id, state);
        return b;
    }

    @Override
    public List<ChinaCity> selectList(QueryWrapper queryWrapper) {
        List<ChinaCity> chinaCities = chinaCityMapper.selectList(null);
        return chinaCities;
    }

    @Override
    public IPage<ChinaCity> selectPage(Page<ChinaCity> page) {
        return chinaCityMapper.selectPage(page,null);

    }

}
