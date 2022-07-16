package com.hzh.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzh.common.pojo.HzhOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    //HzhOrder selectByOrderId(@Param("orderId")String orderId);
}
