package com.hzh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginVo;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.respone.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:53
 */
public interface HzhUserService {

    Result sendEmailCode(String verification, String emailAddress, boolean mustRegister) throws Exception;

    int addUser(HzhUser hzhUser);

    HzhUser findByUserName(String userName);

    HzhUser findByEmail(String email);

    Result login(LoginVo loginVo, String verification) throws Exception;

    HzhUser findByPhoneNum(String phonenumber);

    Result chechToken() throws Exception;

    Result logout() throws Exception;

    Result reSetPasswordBySelf(String mailCode, ReSetPasswordVo reSetPasswordVo) throws Exception;

    IPage<HzhUser> getMemberUserByPage(Page<HzhUser> page);

    HzhUser findByUserId(long id);

    boolean updateByState(long parseLong, String status,String updateDate);

    boolean delUserById(long id, String delFlag,String updateDate);

    Result reSetPasswordByAdmin(String id, ReSetPasswordVo reSetPasswordVo) throws Exception;

    Result registerUser(String mailCode, HzhUser hzhUser) throws Exception;

    Result sendReSetPasswordEmail(String verification, String emailAddress, boolean mustRegister) throws Exception;

    Result initAdminAccount(HzhUser hzhUser);

    IPage<HzhUser> selectListByFilter(Page<HzhUser> page, HzhUser hzhUser);

    int addUserByAdmin(HzhUser hzhUser);

    Map uploadExcel(MultipartFile file) throws Exception;

    List<HzhUser> getAll();

    int insert(List<HzhUser> list);

    IPage<HzhUser> adminUserList(Page<HzhUser> page);
}
