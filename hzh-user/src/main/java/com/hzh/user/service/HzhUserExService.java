package com.hzh.user.service;

import com.hzh.common.pojo.vo.ResultVO;

/**
 * @author Hou Zhonghu
 * @since 2022/7/29 10:45
 */
public interface HzhUserExService {
    ResultVO sendEmailCode(String verfication,String emailAddress) throws Exception;
}
