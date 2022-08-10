package com.hzh.common.pojo.vo;

import lombok.Data;

/**
 * @author Hou Zhonghu
 * @since 2022/8/8 9:44
 */
@Data
public class UserVo {

    protected String id;
    protected String sex;
    protected String salt;
    protected String status;
    protected String avatar;
    protected String userName;

}
