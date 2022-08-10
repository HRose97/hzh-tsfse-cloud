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

    LoginUserVo selectOneByFilter(@Param("userName") String userName,
                                  @Param("email")String email,
                                  @Param("phonenumber")String phonenumber);



    HzhUser selectByFilter(HzhUser hzhUser);


    LoginUserVo selectOneByPhone( @Param("phonenumber")String phonenumber);

    LoginUserVo selectOneByEmail(@Param("email")String email);

    LoginUserVo selectOneByUserName(@Param("userName") String userName);

    boolean updatePasswordByEmail(@Param("email")String email,
                                  @Param("password")String encode);

    boolean updateStateById(@Param("id")long id,
                            @Param("status") String status,
                            @Param("updateDate") String updateDate);

    boolean delUserById(@Param("id")long id,
                        @Param("delFlag") String delFlag,
                        @Param("updateDate") String updateDate);

    boolean reSetPasswordByAdmin(@Param("id")long id,
                                 @Param("updateDate") String updateDate,
                                 @Param("encode") String encode);

}
