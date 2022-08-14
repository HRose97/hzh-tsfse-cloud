package com.hzh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.HzhOrder;

import java.util.List;

/**
 * @author Hou Zhonghu
 * @since 2022/7/7 15:57
 */
public interface OrderService {


    IPage<HzhOrder> selectPage(Page<HzhOrder> page);


    int insert(HzhOrder orderInfoInsert);

    HzhOrder selectByOrderId(String orderId);

    boolean updateById(HzhOrder orderInfoUpdata);

    int updateById(String id, String state);

    List<HzhOrder> selectList(Object o);

    IPage<HzhOrder> getAllOrderByPage(Page<HzhOrder> page);
}
