package com.hzh.common.utils;

import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginUserVo;
import java.util.HashMap;
import java.util.Map;

/**
 * JW工具类相关
 *
 * @author Hou Zhonghu
 * @since 2022/8/3 22:02
 */

public class ClaimsUtils {


    public static Map<String,Object> user2Claims(LoginUserVo loginUserVo) {

        Map<String,Object> claims = new HashMap<>();
        claims.put("id",loginUserVo.getId());
        claims.put("sex",loginUserVo.getSex());
        claims.put("status",loginUserVo.getStatus());
        claims.put("avater",loginUserVo.getAvater());
        claims.put("password",loginUserVo.getPassword());
        claims.put("userName",loginUserVo.getUserName());
        return claims;
    }

    public static HzhUser claims2toUser(Map<String,Object> claims) {
        HzhUser hzhUser = new HzhUser();
        String id = claims.get("id").toString();
        String sex = claims.get("sex").toString();
        String status = claims.get("status").toString();
        String avater = claims.get("avater").toString();
        String password = claims.get("password").toString();
        String userName = claims.get("userName").toString();
        hzhUser.setId(Long.valueOf(id));
        hzhUser.setSex(sex);
        hzhUser.setStatus(status);
        hzhUser.setAvatar(avater);
        hzhUser.setUserName(userName);
        hzhUser.setPassword(password);
        return hzhUser;
    }


}
