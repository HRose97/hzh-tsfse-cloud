package com.hzh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzh.common.mapper.HzhUserMapper;
import com.hzh.common.pojo.HzhUser;
import com.hzh.user.service.HzhUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:54
 */
@Service
public class HzhUserServiceImpl implements HzhUserService {


    @Resource
    private HzhUserMapper hzhUserMapper;

    @Override
    public int addUser(HzhUser hzhUser) {
        int insert = hzhUserMapper.insert(hzhUser);
        return insert;
    }

    @Override
    public HzhUser findByUserName(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        HzhUser hzhUser = hzhUserMapper.selectOne(queryWrapper);
        return hzhUser;
    }

    @Override
    public HzhUser findByEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        HzhUser hzhUseremail = hzhUserMapper.selectOne(queryWrapper);
        return hzhUseremail;
    }
}
