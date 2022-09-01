package com.hzh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginVo;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.respone.R;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    R logout() throws Exception;

    R reSetPasswordBySelf(String mailCode, ReSetPasswordVo reSetPasswordVo) throws Exception;

    IPage<HzhUser> findAllByPage(Page<HzhUser> page);

    HzhUser findByUserId(long id);

    boolean updateByState(long parseLong, String status,String updateDate);

    boolean delUserById(long id, String delFlag,String updateDate);

    R reSetPasswordByAdmin(String id,ReSetPasswordVo reSetPasswordVo) throws Exception;

    R registerUser(String mailCode, HzhUser hzhUser) throws Exception;

    R sendReSetPasswordEmail(String verification, String emailAddress, boolean mustRegister) throws Exception;

    R initAdminAccount(HzhUser hzhUser);

    IPage<HzhUser> selectListByFilter(Page<HzhUser> page, HzhUser hzhUser);

    int addUserByAdmin(HzhUser hzhUser);

    Map uploadExcel(MultipartFile file) throws Exception;

    List<HzhUser> getAll();

    int insert(List<HzhUser> list);
}
