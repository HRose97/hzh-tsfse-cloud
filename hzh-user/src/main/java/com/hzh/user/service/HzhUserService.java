package com.hzh.user.service;

import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.ResultVO;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:53
 */
public interface HzhUserService {


    int addUser(HzhUser hzhUser);

    HzhUser findByUserName(String userName);

    HzhUser findByEmail(String email);

    ResultVO login(HzhUser loginUser, String verification) throws Exception;

    HzhUser findByPhoneNum(String phonenumber);
}
