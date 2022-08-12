package com.hzh.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzh.common.pojo.HzhUserToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hou Zhonghu
 * @since 2022/8/5 10:46
 */
@Mapper
public interface HzhUserTokenMapper extends BaseMapper<HzhUserToken> {


    HzhUserToken selectByUserId(@Param("userId") String userId);

    void updateByUserId(HzhUserToken hzhRefreshToken);

}
