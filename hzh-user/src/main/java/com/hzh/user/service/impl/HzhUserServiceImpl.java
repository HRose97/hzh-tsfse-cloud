package com.hzh.user.service.impl;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.mapper.HzhUserMapper;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginUserVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.service.BaseService;
import com.hzh.common.utils.*;
import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:54
 */
@Service
@Slf4j
public class HzhUserServiceImpl extends BaseService implements HzhUserService {


    @Resource
    private HzhUserMapper hzhUserMapper;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisKeyUtil redisKeyUtil;


    @Override
    public int addUser(HzhUser hzhUserInsert) {
        int insert = hzhUserMapper.insert(hzhUserInsert);
        return insert;
    }

    @Override
    public HzhUser findByUserName(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        HzhUser hzhUser = hzhUserMapper.selectOne(queryWrapper);
        return hzhUser;
    }

    @Override
    public HzhUser findByEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        HzhUser hzhUseremail = hzhUserMapper.selectOne(queryWrapper);
        return hzhUseremail;
    }

    @Override
    public HzhUser findByPhoneNum(String phonenumber) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phonenumber",phonenumber);
        HzhUser hzhUserPhone = hzhUserMapper.selectOne(queryWrapper);
        return hzhUserPhone;
    }

    //登录
    @Override
    public ResultVO login(HzhUser loginUser, String verification) throws Exception {
        log.info("hzhUser ===> " + loginUser);
        log.info("verification ===> " + verification);

       /* // 登录不做  校验图灵验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(verification);
        //这种验证方式是会删除只能验证一次
        ResponseModel response = captchaService.verification(captchaVO);
        String repCode = response.getRepCode();
        //System.out.println("repCode ===> "+repCode);
        if(response.isSuccess() == false){
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员

            //没有具体说明是什么异常
            *//*            if (repCode != null && repCode.equals(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getCode())){
                return ResultVO.ok(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            }else if(repCode == null || !repCode.equals(RepCodeEnum.SUCCESS.getCode())){
                return ResultVO.status(ResultEnum.VERIFICATION_CODE_ERROR1);
            }*//*

            if (repCode != null && repCode.equals(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getCode())){
                return ResultVO.ok(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            }else {
                switch (repCode){
                    case "9999":
                        return ResultVO.status(ResultEnum.VERIFICATION_CODE_ERROR1);
                    case "0011":
                        return ResultVO.status(ResultEnum.VERIFICATION_CODE_ERROR2);
                    case "6110":
                        return ResultVO.status(ResultEnum.VERIFICATION_CODE_ERROR3);
                    case "6111":
                        return ResultVO.status(ResultEnum.VERIFICATION_CODE_ERROR4);
                    case "6112":
                        return ResultVO.status(ResultEnum.VERIFICATION_CODE_ERROR5);
                }
            }
        }
*/
        //校验数据
        String userName = loginUser.getUserName();
        if (StringUtils.isEmpty(userName)){
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }
        //登录的时候不应该判断密码长度 而是匹配密码是否相同  下面有对密码进行对比
/*        String password = loginUser.getPassword();
        if (password.length() < 8){
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }*/

        LoginUserVo hasUserByUserName = null;

        //根据用户名查询是否存在该用户  查询用户 TODO 要先去判断用户通过什么方式登录
        //先判断是否为手机号
        boolean mobile = FormatCheckUtils.isMobile(userName);
        if (mobile){
            String phoneNum = userName;
             hasUserByUserName = hzhUserMapper.selectOneByPhone(phoneNum);
        }
        //在判断是否为邮箱地址
        boolean email = FormatCheckUtils.isEmail(userName);
        if (email){
            String emailIsUserName = userName;
             hasUserByUserName = hzhUserMapper.selectOneByEmail(emailIsUserName);
        }
        //最后都不是则通过userName
         hasUserByUserName = hzhUserMapper.selectOneByUserName(userName);
        if (hasUserByUserName == null ) {
            return ResultVO.status(ResultEnum.LOGIN_ACCOUNT_ERROR);
        }else if (hasUserByUserName.getStatus().equals("1")){
            return ResultVO.status(ResultEnum.LOGIN_DISABLED_USER);
        }else {
            //如果用户存在并且未被禁用
            //对密码进行比对  前端传递未加密  数据库中加密
            boolean matches = passwordEncoder.matches(hasUserByUserName.getPassword(), passwordEncoder.encode(loginUser.getPassword()));
            if (matches){
                String token = createToken(hasUserByUserName);
                log.info("{}此次登录的token === >" +hasUserByUserName.getId(),token);
                if (!token.equals("")){
                    return ResultVO.ok();
                }else {
                    return ResultVO.status(ResultEnum.INNER_EXCEPTION);
                }
            }else {
                return ResultVO.status(ResultEnum.LOGIN_ERROR);
            }
        }

    }

     /**
      * @Author Hou zhonghu
      * @Description  创建token
      *         -token              (有效期 2h  存入redis)
      *         -tokenKey           (Token 的MD5)
      *         -refreshToken       (有效期1个月，放到数据库中)
      * @Date 2022/8/3 19:35
      * @Param HzhUser对象
      * @return token
      **/
    private String createToken(LoginUserVo hasUserByUserName) throws Exception {
        Map<String, Object> claims = ClaimsUtils.user2Claims(hasUserByUserName);
        //创建token  JwtUtil.jwtTtl=== 1H  token入库
        String token = JwtUtil.createToken(claims, 2 * JwtUtil.jwtTtl, JwtUtil.jwtKey);
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //一个月
        String refreshToken = JwtUtil.createRefreshToken(hasUserByUserName.getId(), JwtUtil.jwtTtlOneMonth, JwtUtil.jwtKey);

        //token 入redis  有效期2H
        String userTokenLogin = redisKeyUtil.mkLoginTokenKey(hasUserByUserName);
        redisUtils.set(userTokenLogin, token, 2, TimeUnit.HOURS);

        String cookieRedisKey = redisKeyUtil.mkCookieKey(hasUserByUserName);
        CookieUtils.setUpCookie(getResponse(),cookieRedisKey,tokenKey);

        //原视频在  P48 40min左右 存入数据库表中，我设计的没有相关数据表，存入redis中
        HashMap<String,String> refreshMap = new HashMap<>();
        refreshMap.put("refreshToken",refreshToken);
        refreshMap.put("tokenKey",tokenKey);
        refreshMap.put("userId", hasUserByUserName.getId());
        redisUtils.hPutAll(hasUserByUserName.getId(),refreshMap);

        return token;
    }

}
