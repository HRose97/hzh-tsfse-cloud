package com.hzh.common.pojo.vo;

import lombok.Data;

/**
 * @author Hou Zhonghu
 * @since 2022/8/11 17:01
 */
@Data
public class LoginVo{

    private String id;
    private String email;
    private String sex;
    private String salt;
    private String status;
    private String userName;
    private String password;


}
