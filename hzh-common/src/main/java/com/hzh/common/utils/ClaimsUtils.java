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
        claims.put("salt",loginUserVo.getSalt());
        claims.put("status",loginUserVo.getStatus());
        claims.put("avatar",loginUserVo.getAvatar());
        claims.put("password",loginUserVo.getPassword());
        claims.put("userName",loginUserVo.getUserName());
        return claims;
    }

    public static LoginUserVo claims2toUser(Map<String,Object> claims) {
        LoginUserVo hzhUser = new LoginUserVo();
        String id = claims.get("id").toString();
        String sex = claims.get("sex").toString();
        String salt = claims.get("salt").toString();
        String status = claims.get("status").toString();
        String avatar = claims.get("avatar").toString();
        String password = claims.get("password").toString();
        String userName = claims.get("userName").toString();
        hzhUser.setId(String.valueOf(Long.valueOf(id)));
        hzhUser.setSex(sex);
        hzhUser.setSalt(salt);
        hzhUser.setStatus(status);
        hzhUser.setAvatar(avatar);
        hzhUser.setUserName(userName);
        hzhUser.setPassword(password);
        return hzhUser;
    }


}
