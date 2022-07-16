package com.hzh.common.mapper;

import com.hzh.common.pojo.ChinaCity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ChinaCityMapper extends BaseMapper<ChinaCity> {

    int updateById(@Param("id") long id, @Param("state") String state);
}
