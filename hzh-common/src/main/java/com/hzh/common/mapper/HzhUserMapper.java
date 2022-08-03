package com.hzh.common.mapper;

import com.hzh.common.pojo.HzhUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzh.common.pojo.vo.LoginUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    LoginUserVo selectOneByFilter(@Param("userName") String userName);

    HzhUser selectByFilter();


}
