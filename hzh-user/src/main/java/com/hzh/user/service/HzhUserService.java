package com.hzh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginVo;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.respone.R;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:53
 */
public interface HzhUserService {

    R sendEmailCode(String verification, String emailAddress, boolean mustRegister) throws Exception;

    int addUser(HzhUser hzhUser);

    HzhUser findByUserName(String userName);

    HzhUser findByEmail(String email);

    R login(LoginVo loginVo, String verification) throws Exception;

    HzhUser findByPhoneNum(String phonenumber);

    R chechToken() throws Exception;

    ResultVO logout() throws Exception;

    ResultVO reSetPassword(String mailCode, ReSetPasswordVo reSetPasswordVo) throws Exception;

    IPage<HzhUser> findAllByPage(Page<HzhUser> page);

    HzhUser findByFitler(long id);

    boolean updateByState(long parseLong, String status,String updateDate);

    boolean delUserById(long id, String delFlag,String updateDate);

    ResultVO reSetPasswordByAdmin(String id,ReSetPasswordVo reSetPasswordVo) throws Exception;

    R registerUser(String mailCode, HzhUser hzhUser) throws Exception;


}
