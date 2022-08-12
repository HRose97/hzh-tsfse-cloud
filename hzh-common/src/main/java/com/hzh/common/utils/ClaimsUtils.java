package com.hzh.common.utils;

import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginBean;
import com.hzh.common.pojo.vo.UserVo;

import java.util.HashMap;
import java.util.Map;

/**
 * JW工具类相关
 *
 * @author Hou Zhonghu
 * @since 2022/8/3 22:02
 */

public class ClaimsUtils {


    public static Map<String,Object> user2Claims(HzhUser loginUserVo) {

        Map<String,Object> claims = new HashMap<>();
        claims.put("id",loginUserVo.getId());
        claims.put("sex",loginUserVo.getSex());
        claims.put("salt",loginUserVo.getSalt());
        claims.put("status",loginUserVo.getStatus());
        claims.put("phonenumber",loginUserVo.getPhonenumber());
        claims.put("avatar",loginUserVo.getAvatar());
        claims.put("userName",loginUserVo.getUserName());
        return claims;
    }

    public static UserVo claims2toUser(Map<String,Object> claims) {
        UserVo hzhUser = new UserVo();
        String id = claims.get("id").toString();
        String sex = claims.get("sex").toString();
        String phonenumber = claims.get("phonenumber").toString();
        String status = claims.get("status").toString();
        String avatar = claims.get("avatar").toString();
        String userName = claims.get("userName").toString();
        hzhUser.setId(id);
        hzhUser.setSex(sex);
        hzhUser.setPhonenumber(phonenumber);
        hzhUser.setStatus(status);
        hzhUser.setAvatar(avatar);
        hzhUser.setUserName(userName);
        return hzhUser;
    }


}
