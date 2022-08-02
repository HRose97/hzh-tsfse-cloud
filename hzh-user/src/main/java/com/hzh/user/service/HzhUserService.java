package com.hzh.user.service;

import com.hzh.common.pojo.HzhUser;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:53
 */
public interface HzhUserService {


    int addUser(HzhUser hzhUser);

    HzhUser findByUserName(String userName);

    HzhUser findByEmail(String email);
}
