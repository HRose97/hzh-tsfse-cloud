package com.hzh.user.service.impl;

import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.mapper.HzhUserMapper;
import com.hzh.common.mapper.HzhUserTokenMapper;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.HzhUserToken;
import com.hzh.common.pojo.vo.LoginUserVo;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.pojo.vo.UserVo;
import com.hzh.common.service.BaseService;
import com.hzh.common.utils.*;
import com.hzh.user.service.HzhUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:54
 */
@Service
@Slf4j
public class HzhUserServiceImpl extends BaseService implements HzhUserService {


    private static  final String cookieTokenName = "tsfseToken";

    @Resource
    private HzhUserMapper hzhUserMapper;

    @Resource
    private HzhUserTokenMapper hzhUserTokenMapper;

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

    //解析token
    @Override
    public ResultVO chechToken() throws Exception {
        //先是拿到tokenkey
        String tokenKey = CookieUtils.getCookie(getRequest(), cookieTokenName);
        if (StringUtils.isEmpty(tokenKey)){
            //如果为空 账号为登录
            return ResultVO.status(ResultEnum.NOT_LOGIN_USER);
        }
        //去rendis中拿数据 拿token 有可能超过2H
        String token = redisKeyUtil.mkLoginTokenKey(tokenKey);
        String saltKey = redisKeyUtil.mkUserTokenSaltKey(tokenKey);
        String salt = redisUtils.get(saltKey);
        if (StringUtils.isEmpty(token)) {
            return ResultVO.status(ResultEnum.NOT_LOGIN_USER);
        }
        if (!StringUtils.isEmpty(token)){
            //有的话解析token
            try{
                Claims claims = JwtUtil.parseJWT(token, salt);
                UserVo loginUserVo = ClaimsUtils.claims2toUser(claims);
                return ResultVO.ok(loginUserVo);
            }catch (Exception e){
                e.printStackTrace();
                log.error("token 解析异常");
                //走检查refreshToken的路
                return  chechResfreshToken(tokenKey,salt);
            }
        }else {
            //走检查refreshToken的路
            return  chechResfreshToken(tokenKey,salt);
        }
    }

    //退出登录
    @Override
    public ResultVO logout() throws Exception {
        String tokenkey = CookieUtils.getCookie(getRequest(), cookieTokenName);
        if (StringUtils.isEmpty(tokenkey)){
            return ResultVO.status(ResultEnum.NOT_LOGIN_USER);
        }else {
            //删除token
            String mkLoginTokenKey = redisKeyUtil.mkLoginTokenKey(tokenkey);
            redisUtils.delete(mkLoginTokenKey);
            //删除refseToken
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tokey_key",tokenkey);
            hzhUserTokenMapper.delete(queryWrapper);
            //删除cookie
            CookieUtils.setUpCookie(getResponse(),cookieTokenName,"");
            //删除redis中的盐值
            String token = redisKeyUtil.mkLoginTokenKey(tokenkey);
            String saltKey = redisKeyUtil.mkUserTokenSaltKey(token);
            redisUtils.delete(saltKey);
            return ResultVO.ok("退出登录成功");
        }
    }

    //让用户强制下线
    public ResultVO logoutById(String userId) throws Exception {
        //可以通过userId 到refreshToken的表中查询到相关数据，然后退出登录
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        HzhUserToken hzhUserToken = hzhUserTokenMapper.selectOne(queryWrapper);

        if (hzhUserToken == null){
            return ResultVO.ok("退出登录成功");
        }else {
            //删除token
            String mkLoginTokenKey = redisKeyUtil.mkLoginTokenKey( hzhUserToken.getTokenKey());
            redisUtils.delete(mkLoginTokenKey);

            //删除refseToken  根据userid直接删除
            hzhUserTokenMapper.delete(queryWrapper);
            //删除cookie
            CookieUtils.setUpCookie(getResponse(),cookieTokenName,"");
            //删除redis中的盐值
            String token = redisKeyUtil.mkLoginTokenKey(hzhUserToken.getTokenKey());
            String saltKey = redisKeyUtil.mkUserTokenSaltKey(token);
            redisUtils.delete(saltKey);
            return ResultVO.ok("退出登录成功");
        }
    }



    /**
     * 从数据库中找到refretoken  如果没有那就真的没有登录
     * 如果有，判断是否有过期
     * @param tookenKey
     * @return
     */
    private ResultVO chechResfreshToken(String tookenKey,String salt) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token_key",tookenKey);
        queryWrapper.select("refresh_token","user_id");
        //查不到
        HzhUserToken hzhUserRefreshToken = hzhUserTokenMapper.selectOne(queryWrapper);
        if (hzhUserRefreshToken == null){
            return ResultVO.okAndError(ResultEnum.NOT_LOGIN_USER);
        }else {
            try {
                //如果没过期，通过用户ID查到用户内容，在创建token
                JwtUtil.parseJWT(hzhUserRefreshToken.getRefreshToken(), salt);

                //先删除原来的数据 redis和数据库中的
                redisUtils.delete(tookenKey);
                String userId = hzhUserRefreshToken.getUserId();
                QueryWrapper<HzhUserToken> tokenQueryWrapper = new QueryWrapper<>();
                tokenQueryWrapper.eq("user_id",userId);
                hzhUserTokenMapper.delete(queryWrapper);

                //创建新的token
                HzhUser hzhUser = hzhUserMapper.selectById(userId);
                //返回给前端的不能有密码
                UserVo loginUserVo = new UserVo();
                loginUserVo.setId(userId);
                loginUserVo.setUserName(hzhUser.getUserName());
                loginUserVo.setSex(hzhUser.getSex());
                loginUserVo.setAvatar(hzhUser.getAvatar());
                loginUserVo.setStatus(hzhUser.getStatus());
                //为了安全盐值返回为null
                loginUserVo.setSalt(hzhUser.getSalt());
                //createToken(loginUserVo);
                return ResultVO.ok(loginUserVo);
            }catch (Exception e){
                e.printStackTrace();
                //过期就是未登录
            }
        }
        return ResultVO.status(ResultEnum.NOT_LOGIN_USER);
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
        //对密码校验，前端传递来的密码不可能为明文传递通过MD5加密
        String password = loginUser.getPassword();
        if (password.length() != 32){
            return ResultVO.status(ResultEnum.PASSWORD_MD5_ERROR);
        }

        LoginUserVo hasUserByUserName = null;

        //根据用户名查询是否存在该用户  查询用户
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
            //前端密码经过加密后再次经过passwordEncoder 加密后存入库中
            boolean matches = passwordEncoder.matches(password, hasUserByUserName.getPassword());
            if (matches){
                createToken(hasUserByUserName);
                UserVo userVo = new UserVo();
                userVo.setAvatar(hasUserByUserName.getAvatar());
                userVo.setUserName(hasUserByUserName.getUserName());
                return ResultVO.ok(userVo);
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
      *         UserVo 返回给前端不能有密码
      * @Date 2022/8/3 19:35
      * @Param HzhUser对象
      * @return token
      **/
    private String createToken(UserVo hasUserByUserName) throws Exception {
        Map<String, Object> claims = ClaimsUtils.user2Claims(hasUserByUserName);
        //创建token  JwtUtil.jwtTtl=== 1H  token入库
        String token = JwtUtil.createToken(claims, Constants.TimeSecond.TWO_HOUR, hasUserByUserName.getSalt());
        //返回给用户
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //一个月  refreshToken 入库
        String refreshToken = JwtUtil.createRefreshToken(hasUserByUserName.getId(), Constants.Millions.MOUTH, hasUserByUserName.getSalt());

        //token 入redis  有效期2H   token 加前缀
        String userTokenLogin = redisKeyUtil.mkLoginTokenKey(tokenKey);
        redisUtils.set(userTokenLogin, token, 2, TimeUnit.HOURS);

        //返回给用户  tokenKey  cookie是固定的 不能根据登录的用户不同有不同的cookie name
        CookieUtils.setUpCookie(getResponse(),cookieTokenName,tokenKey);

        //refreshToken 入库
        HzhUserToken hzhRefreshToken = new HzhUserToken();
        hzhRefreshToken.setUserId(hasUserByUserName.getId());
        hzhRefreshToken.setTokenKey(tokenKey);
        hzhRefreshToken.setRefreshToken(refreshToken);
        HzhUserToken hzhUserToken = hzhUserTokenMapper.selectById(hasUserByUserName.getId());
        if (hzhUserToken == null){
            hzhUserTokenMapper.insert(hzhRefreshToken);
        }else {
            hzhUserTokenMapper.updateById(hzhRefreshToken);
        }

        //将盐值保存到redis中，方便在解析token时获取
        String saltKey = redisKeyUtil.mkUserTokenSaltKey(tokenKey);
        //时间至少有2h  有效期一天
        redisUtils.set(saltKey,hasUserByUserName.getSalt(),1,TimeUnit.DAYS);

        return token;
    }

    //修改密码
    @Override
    public ResultVO reSetPassword(String mailCode, ReSetPasswordVo reSetPasswordVo) throws Exception {

        if (StringUtils.isEmpty(mailCode) || reSetPasswordVo == null){
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }else {
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email",reSetPasswordVo.getEmail());
            HzhUser hzhUser = hzhUserMapper.selectOne(queryWrapper);
            if (hzhUser != null){
                //对验证码进行比对
                String registerEmailRedisKey = redisKeyUtil.mkRegisterEmailRedisKey(reSetPasswordVo.getEmail());
                String code = redisUtils.get(registerEmailRedisKey);
                if (code.equals(mailCode)){
                    String encode = passwordEncoder.encode(reSetPasswordVo.getPassword());
                    boolean update = hzhUserMapper.updatePasswordByEmail(reSetPasswordVo.getEmail(),encode);
                    if (update){
                        //跟新成功以后退出登录
                        logout();
                        return ResultVO.ok("密码修改成功");
                    }else {
                        return ResultVO.status(ResultEnum.UPDATE_PASSWORD_ERROR);
                    }
                }else {
                    return ResultVO.status(ResultEnum.CODE_ERROR);
                }
            }else {
                return ResultVO.status(ResultEnum.REGUST_EMAIL_NOTISEXIST);
            }
        }
    }

    @Override
    public IPage<HzhUser> findAllByPage(Page<HzhUser> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag",0);
        Page<HzhUser> hzhUserPage = hzhUserMapper.selectPage(page, queryWrapper);
        return hzhUserPage;
    }

    @Override
    public HzhUser findByFitler(long id) {
        return hzhUserMapper.selectById(id);
    }

    @Override
    public boolean updateByState(long id, String status,String updateDate) {
        boolean undateState = hzhUserMapper.updateStateById(id,status,updateDate);
        return undateState;
    }

    @Override
    public boolean delUserById(long id, String delFlag,String updateDate) {
        boolean delById = hzhUserMapper.delUserById(id,delFlag,updateDate);
        return delById;
    }

    /**
     *
     * 管理员重置用户
     * 对密码进行处理
     * 修改密码
     * 让用户退出登录
     * @param id
     * @return
     */
    @Override
    public ResultVO reSetPasswordByAdmin(String id,ReSetPasswordVo reSetPasswordVo) throws Exception {
        String updateDate = DateUtils.getCurrent(DateUtils.dateFullPattern);

        HzhUser hzhUser = hzhUserMapper.selectById(id);
        if (hzhUser == null){
            return ResultVO.status(ResultEnum.LOGIN_ACCOUNT_ERROR);
        }

        if (StringUtils.isEmpty(reSetPasswordVo.getPassword()) || reSetPasswordVo.getPassword().length() != 32){
            return ResultVO.status(ResultEnum.PASSWORD_ERROR);
        }else {
            String encode = passwordEncoder.encode(reSetPasswordVo.getPassword());
            boolean b = hzhUserMapper.reSetPasswordByAdmin(Long.parseLong(id), updateDate, encode);
            if (b){
                ResultVO resultVO = logoutById(id);
                return resultVO;
            }
        }
        return ResultVO.status(ResultEnum.INNER_EXCEPTION);
    }


}
