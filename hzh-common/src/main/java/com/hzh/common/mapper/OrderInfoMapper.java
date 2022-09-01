package com.hzh.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.HzhOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<HzhOrder> {

    int updateById(@Param("id") String id, @Param("state") String state);

    //List<HzhOrder> selectOrderInfoByPage(@Param("current") long current, @Param("size") long size);

    //HzhOrder selectByOrderId(@Param("orderId")String orderId);
}
