package com.hzh.common.pojo.vo;

import lombok.Data;

/**
 * @author Hou Zhonghu
 * @since 2022/8/3 22:06
 */
@Data
public class LoginUserVo {

    private String id;
    private String sex;
    private String salt;
    private String status;
    private String avatar;
    private String password;
    private String userName;


}
