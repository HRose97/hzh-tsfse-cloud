package com.hzh.user.controller;

import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.utils.*;
import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:50
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class HzhUserController {

    @Resource
    private HzhUserService hzhUserService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisKeyUtil redisKeyUtil;

    @Resource
    private PasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test")
    public String test(){
        return "测试";
    }

    @PostMapping("/user/register")
    public ResultVO register(@RequestParam(value = "mailCode",required = false)String mailCode,@RequestBody Map<String,Object> userMap) throws Exception {

        //TODO 前端post传递参数为空，为了继续学习，直接返回字符串

        HashMap<String,Object> result = new HashMap<>();
        String currentdate = DateUtils.getCurrent(DateUtils.dateFullPattern);

        String event =  null == userMap.get("event") ? "1" : userMap.get("event").toString();
        Long userId = null == userMap.get("id") ? -1 : Long.parseLong(userMap.get("id").toString());
        String userName = null == userMap.get("userName") ? "" : userMap.get("userName").toString();
        String password = null == userMap.get("password") ? "" : userMap.get("password").toString();
        String userDescription = null == userMap.get("userDescription") ? "" : userMap.get("userDescription").toString();
        String status = null == userMap.get("status") ? "" : userMap.get("status").toString();
        String email = null == userMap.get("email") ? "" : userMap.get("email").toString();
        String phonenumber = null == userMap.get("phonenumber") ? "" : userMap.get("phonenumber").toString();
        String sex = null == userMap.get("sex") ? "" : userMap.get("sex").toString();
        String avatar = null == userMap.get("avatar") ? "" : userMap.get("avatar").toString();
        String userType = null == userMap.get("userType") ? "" : userMap.get("userType").toString();
        String createTime = null == userMap.get("createTime") ? "" : userMap.get("createTime").toString();
        Long updateBy = null == userMap.get("updateBy") ? -1 : Long.parseLong(userMap.get("updateBy").toString());
        String updateTime = null == userMap.get("updateTime") ? "" : userMap.get("updateTime").toString();
        String delFlag = null == userMap.get("delFlag") ? "0" : userMap.get("delFlag").toString();

        // TODO 判断验证码是否正确
        String codeRedisKey = redisKeyUtil.mkRegisterCodeRedisKey(email);
        String redisCode = redisUtils.get(codeRedisKey);
        if (!redisCode.equals(mailCode)){
            return ResultVO.ok("验证码不正确");
        }
        // 判断用户名是否被使用
        HzhUser hasUsername = hzhUserService.findByUserName(userName);
        if (hasUsername.getUserName().equals(userName)){
            return ResultVO.ok("用户名已存在");
        }
        //判断邮箱是否以已经注册
        HzhUser hasEmail = hzhUserService.findByEmail(email);
        if (hasEmail.getEmail().equals(email)){
            return ResultVO.ok("该邮箱已注册");
        }

        boolean b = FormatCheckUtils.checkPasswordRule(password, userName);
        if (password.length() < 8  ){
            return ResultVO.ok("请至少输入8位数密码");
        }if (!b){
            return ResultVO.ok("密码不符和规则，请输入包括大小写字母、数字、特殊符号中的3种");
        }

        HzhUser hzhUser = new HzhUser();
        hzhUser.setUserName(userName);
        hzhUser.setPassword(bCryptPasswordEncoder.encode(password));
        hzhUser.setUserDescription(userDescription);
        hzhUser.setStatus(Constants.User.UNFORBIDDENT_STATE);
        hzhUser.setEmail(email);
        hzhUser.setPhonenumber(phonenumber);
        hzhUser.setSex(sex);
        //设置默认头像
        hzhUser.setAvatar("https://www.manpingou.com/uploads/allimg/170221/25-1F221135231E5.jpg");
        hzhUser.setUserType(userType);
        hzhUser.setCreateTime(currentdate);
        hzhUser.setUpdateBy(updateBy);
        hzhUser.setUpdateTime(currentdate);
        hzhUser.setDelFlag(Constants.User.UNFORBIDDENT_STATE);

        int add = hzhUserService.addUser(hzhUser);
        if(add == 1){
            result.put("res","1");
            result.put("msg","用户注册成功");
            return ResultVO.ok(result);
        }else {
            result.put("res","0");
            result.put("msg","用户注册失败");
            return ResultVO.ok(result);
        }
        //return ResultVO.ok("OK");
    }


}
