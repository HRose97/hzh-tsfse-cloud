package com.hzh.common.mapper;

import com.hzh.common.pojo.EventInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * <p>
 * 体育赛事举办信息表 Mapper 接口
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Mapper
public interface EventInfoMapper extends BaseMapper<EventInfo> {

    int updateById(int physicalId, int physicalStatus);

    int getMaxId();
}
