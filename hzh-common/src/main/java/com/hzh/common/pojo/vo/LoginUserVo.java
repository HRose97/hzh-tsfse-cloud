package com.hzh.common.pojo.vo;

import lombok.Data;

/**
 *
 * 登录Bean 类 因为返回个前端的数据有密码和盐值
 * 不安全
 * @author Hou Zhonghu
 * @since 2022/8/3 22:06
 */
@Data
public class LoginUserVo extends UserVo{

   private String password;



}
