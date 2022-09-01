package com.hzh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzh.common.mapper.OrderInfoMapper;
import com.hzh.common.pojo.HzhOrder;
import com.hzh.order.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hou Zhonghu
 * @since 2022/7/7 15:57
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper,HzhOrder> implements OrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public IPage<HzhOrder> selectPage(Page<HzhOrder> page) {
        return orderInfoMapper.selectPage(page,null);
    }

    @Override
    public int insert(HzhOrder orderInfoInsert) {
        return orderInfoMapper.insert(orderInfoInsert);
    }

    @Override
    public HzhOrder selectByOrderId(String orderId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        HzhOrder hzhOrder = orderInfoMapper.selectOne(queryWrapper);
        return hzhOrder;
    }

    @Override
    public boolean updateById(HzhOrder orderInfoUpdata) {
        int i = orderInfoMapper.updateById(orderInfoUpdata);
        if (i == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int updateById(String orderId, String state) {
        return orderInfoMapper.updateById(orderId,state);
    }

    @Override
    public List<HzhOrder> selectList(Object o) {
        return orderInfoMapper.selectList(null);
    }

    //分页查询订单
    @Override
    public IPage<HzhOrder> getAllOrderByPage(Page<HzhOrder> page) {
        Page<HzhOrder> hzhOrderPage = orderInfoMapper.selectPage(page, null);
        //IPage<HzhOrder> hzhOrderPage = orderInfoMapper.selectOrderInfoByPage(page.getCurrent(),page.getSize());
        return hzhOrderPage;
    }
}
