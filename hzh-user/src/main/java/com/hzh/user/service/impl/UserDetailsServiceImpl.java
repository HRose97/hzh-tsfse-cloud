package com.hzh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzh.common.mapper.HzhUserMapper;
import com.hzh.common.pojo.HzhUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * UserDetail实现类
 *
 * @author Hou Zhonghu
 * @since 2022/7/27 10:17
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Resource
    private HzhUserMapper hzhUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //执行自定义登录逻辑
        System.out.println("执行自定义登录逻辑");

        //1.根据用户名去数据库查询，如果不存在则抛出UsernameNotFoundException异常
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        HzhUser hzhUser = hzhUserMapper.selectOne(queryWrapper);
        //如果不存在
        if (hzhUser == null ){
            throw  new UsernameNotFoundException("用户名：" + username + "不存在");
        }

        //2.用户名存在 比较密码（注册时已经加密过），如果匹配成功返回UserDetails
        //String encode = passwordEncoder.encode(hzhUser.getPassword());
        //passwordEncoder.matches(encode,)

        //可以配置多个权限用逗号分隔；
        return new User(username,hzhUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal,check"));
    }
}
