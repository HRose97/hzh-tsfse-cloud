package com.hzh.common.mapper;

import com.hzh.common.pojo.HzhUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Mapper
public interface HzhUserMapper extends BaseMapper<HzhUser> {

}
