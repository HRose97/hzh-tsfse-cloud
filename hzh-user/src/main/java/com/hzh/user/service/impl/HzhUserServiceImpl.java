package com.hzh.user.service.impl;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.mapper.HzhUserMapper;
import com.hzh.common.mapper.HzhUserTokenMapper;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.HzhUserToken;
import com.hzh.common.pojo.vo.*;
import com.hzh.common.respone.R;
import com.hzh.common.service.BaseService;
import com.hzh.common.utils.*;
import com.hzh.user.service.HzhUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:54
 */
@Service
@Slf4j
public class HzhUserServiceImpl extends BaseService implements HzhUserService {


    private static  final String cookieTokenName = "tsfseToken";

    String date = DateUtils.getCurrent(DateUtils.year_of_minute);

    @Resource
    private CaptchaService captchaService;

    @Resource
    private HzhUserMapper hzhUserMapper;

    @Resource
    private HzhUserTokenMapper hzhUserTokenMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisKeyUtil redisKeyUtil;

    @Resource
    private PasswordEncoder bCryptPasswordEncoder;

    //新用户发送邮箱验证码
    @Override
    public R sendEmailCode(String verification, String emailAddress, boolean mustRegister) throws Exception {
        String date = DateUtils.getCurrent(DateUtils.ticketPattern);
        log.info("String verfication ===> " +  verification);

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
            if (repCode != null && repCode.equals(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getCode())){
                return R.SUCCESS(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            }else if(repCode == null || !repCode.equals(RepCodeEnum.SUCCESS.getCode())){
                return R.FAILED("图灵码验证失败");
            }
        }

        log.info("email address {},",emailAddress);
        //检查数据是否正确
        if (TextUtils.isEmpty(emailAddress)) {
            //不可以为空
            return R.FAILED("邮箱地址不可以为空");
        }
        //对邮箱地址校验
        boolean isEmailTrue = FormatCheckUtils.isEmail(emailAddress);
        if (!isEmailTrue){
            //邮箱格式不正确
            return R.FAILED("邮箱格式不正确，请检查邮箱格式");
        }

        //ip地址  继承关系可以直接使用方法
        String remoteAddr = getRequest().getRemoteAddr();
        remoteAddr = remoteAddr.replaceAll(":", "-");
        log.info("客户端Ip地址为 ...{}",remoteAddr);

        //通过ip地址判断是否频繁发送
        String registerIpRedisKey = redisKeyUtil.mkRegisterIPRedisKey(remoteAddr);
        String ipKeyTimes = redisUtils.get(registerIpRedisKey);
        if (!StringUtils.isEmpty(ipKeyTimes)){
            int i = Integer.parseInt(ipKeyTimes);
            log.info("当前ip{}调用了{}次",remoteAddr,i);
            if (i > 100){
                return R.FAILED("请不要通过此ip"+remoteAddr+"频繁发送");
            }else {
                i++;
                //2  时间数量   TimeUnit.HOURS 时间单位
                redisUtils.set(registerIpRedisKey,String.valueOf(i), 2, TimeUnit.HOURS);
            }
        }else {
            //俩小时内有校
            redisUtils.set(registerIpRedisKey,"1",2,TimeUnit.HOURS);
        }

        //通过邮箱账号判断是否频繁发送
        String registerEmailRedisKey = redisKeyUtil.mkRegisterEmailRedisKey(emailAddress);
        String emailKeyTimes = redisUtils.get(registerEmailRedisKey);
        if (!StringUtils.isEmpty(emailKeyTimes)){
            int i = Integer.parseInt(emailKeyTimes);
            log.info("当前邮箱{}已经调用了{}次",emailAddress,i);
            //TODO 次数是属于配置项 一般都不是写死得 需要抽取出来到可配置得地方
            if (i > 100){
                return R.FAILED("请不要通过此邮箱账号"+emailAddress+"频繁发送");
            }else {
                i++;
                //2  时间数量   TimeUnit.HOURS 时间单位
                redisUtils.set(registerEmailRedisKey,String.valueOf(i), 2, TimeUnit.HOURS);
            }
        }else {
            //俩小时内有校
            redisUtils.set(registerEmailRedisKey,"1",2, TimeUnit.HOURS);
        }
        //产生验证码，记录验证码
        Random random = new Random();
        //产生6位数  取值范围（0，999999） 有可能只有1位  可以通过+100000的方式得到俩位数的验证码
        int mailCode = random.nextInt(999999);
        if (mailCode < 100000){
            mailCode += 100000;
        }
        log.info("六位数的验证码为：{}",mailCode);
        //保存到redis中  验证码5min有效
        String codeRedisKey = redisKeyUtil.mkRegisterCodeRedisKey(emailAddress);
        redisUtils.set(codeRedisKey, date +"的注册验证码："+mailCode,5,TimeUnit.MINUTES);
        //TODO  实际注册时放开  发送验证码
        //EmailSender.sendEmailSendCode(emailAddress, "注册验证码："+ mailCode +",5分钟内有效！");

        return R.SUCCESS("邮箱验证发送成功");
    }

    //找回密码发送邮箱验证码
    @Override
    public R sendReSetPasswordEmail(String verification, String emailAddress, boolean mustRegister) throws Exception {
        String date = DateUtils.getCurrent(DateUtils.ticketPattern);
        log.info("String verfication ===> " +  verification);

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
            if (repCode != null && repCode.equals(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getCode())){
                return R.SUCCESS(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            }else if(repCode == null || !repCode.equals(RepCodeEnum.SUCCESS.getCode())){
                return R.FAILED("图灵码验证失败");
            }
        }

        log.info("email address {},",emailAddress);
        //检查数据是否正确
        if (TextUtils.isEmpty(emailAddress)) {
            //不可以为空
            return R.FAILED("邮箱地址不可以为空");
        }
        //对邮箱地址校验
        boolean isEmailTrue = FormatCheckUtils.isEmail(emailAddress);
        if (!isEmailTrue){
            //邮箱格式不正确
            return R.FAILED("邮箱格式不正确，请检查邮箱格式");
        }
        //mustRegister 不管是trut or false 都要对邮箱进行检查是否存在
        QueryWrapper<HzhUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",emailAddress);
        queryWrapper.select("id");
        HzhUser selectOne = hzhUserMapper.selectOne(queryWrapper);
        if (selectOne != null && mustRegister){
            return R.FAILED("该邮箱已经注册");
        }
        if (selectOne == null && !mustRegister){
            return R.FAILED("该邮箱未注册");
        }


        //ip地址  继承关系可以直接使用方法
        String remoteAddr = getRequest().getRemoteAddr();
        remoteAddr = remoteAddr.replaceAll(":", "-");
        log.info("客户端Ip地址为 ...{}",remoteAddr);

        //通过ip地址判断是否频繁发送
        String registerIpRedisKey = redisKeyUtil.mkRegisterIPRedisKey(remoteAddr);
        String ipKeyTimes = redisUtils.get(registerIpRedisKey);
        if (!StringUtils.isEmpty(ipKeyTimes)){
            int i = Integer.parseInt(ipKeyTimes);
            log.info("当前ip{}调用了{}次",remoteAddr,i);
            if (i > 100){
                return R.FAILED("请不要通过此ip"+remoteAddr+"频繁发送");
            }else {
                i++;
                //2  时间数量   TimeUnit.HOURS 时间单位
                redisUtils.set(registerIpRedisKey,String.valueOf(i), 2, TimeUnit.HOURS);
            }
        }else {
            //俩小时内有校
            redisUtils.set(registerIpRedisKey,"1",2,TimeUnit.HOURS);
        }

        //通过邮箱账号判断是否频繁发送
        String registerEmailRedisKey = redisKeyUtil.mkRegisterEmailRedisKey(emailAddress);
        String emailKeyTimes = redisUtils.get(registerEmailRedisKey);
        if (!StringUtils.isEmpty(emailKeyTimes)){
            int i = Integer.parseInt(emailKeyTimes);
            log.info("当前邮箱{}已经调用了{}次",emailAddress,i);
            //TODO 次数是属于配置项 一般都不是写死得 需要抽取出来到可配置得地方
            if (i > 100){
                return R.FAILED("请不要通过此邮箱账号"+emailAddress+"频繁发送");
            }else {
                i++;
                //2  时间数量   TimeUnit.HOURS 时间单位
                redisUtils.set(registerEmailRedisKey,String.valueOf(i), 2, TimeUnit.HOURS);
            }
        }else {
            //俩小时内有校
            redisUtils.set(registerEmailRedisKey,"1",2, TimeUnit.HOURS);
        }
        //产生验证码，记录验证码
        Random random = new Random();
        //产生6位数  取值范围（0，999999） 有可能只有1位  可以通过+100000的方式得到俩位数的验证码
        int mailCode = random.nextInt(999999);
        if (mailCode < 100000){
            mailCode += 100000;
        }
        log.info("六位数的验证码为：{}",mailCode);
        //保存到redis中  验证码5min有效
        String codeRedisKey = redisKeyUtil.mkForgotCodeRedisKey(emailAddress);
        redisUtils.set(codeRedisKey, date +"的找回密码得验证码："+mailCode,5,TimeUnit.MINUTES);
        //TODO  实际注册时放开  发送验证码
        //EmailSender.sendEmailSendCode(emailAddress, "注册验证码："+ mailCode +",5分钟内有效！");

        return R.SUCCESS("邮箱验证发送成功");
    }

    //注册用户
    @Override
    public int addUser(HzhUser hzhUserInsert) {
        int insert = hzhUserMapper.insert(hzhUserInsert);
        return insert;
    }

    //根据用户民查询用户
    @Override
    public HzhUser findByUserName(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        HzhUser hzhUser = hzhUserMapper.selectOne(queryWrapper);
        return hzhUser;
    }

    //根据email查询用户
    @Override
    public HzhUser findByEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        HzhUser hzhUseremail = hzhUserMapper.selectOne(queryWrapper);
        return hzhUseremail;
    }

    //根据手机号查询用户
    @Override
    public HzhUser findByPhoneNum(String phonenumber) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phonenumber",phonenumber);
        HzhUser hzhUserPhone = hzhUserMapper.selectOne(queryWrapper);
        return hzhUserPhone;
    }

    //解析token
    @Override
    public R chechToken() throws Exception {
        //先是拿到tokenkey
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.TSFSE_TOKEN);
        if (StringUtils.isEmpty(tokenKey)){
            //如果为空 账号为登录
            return R.NOT_LOGIN();
        }
        //去rendis中拿数据 拿token 有可能超过2H
        String token = redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        //获取盐值
        String saltKey = redisKeyUtil.mkUserTokenSaltKey(tokenKey);
        String salt = redisUtils.get(saltKey);
        if (StringUtils.isEmpty(salt)) {
            return R.FAILED("账号未登录");
        }
        if (!StringUtils.isEmpty(token)){
            //有的话解析token
            try{
                Claims claims = JwtUtil.parseJWT(token, salt);
                UserVo userVo = ClaimsUtils.claims2toUser(claims);
                return R.SUCCESS("已登录",userVo);
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
    public R logout() throws Exception {
        String tokenkey = CookieUtils.getCookie(getRequest(), cookieTokenName);
        if (StringUtils.isEmpty(tokenkey)){
            return R.NOT_LOGIN();
        }else {
            //删除token
            redisUtils.delete(Constants.User.KEY_TOKEN + tokenkey);
            //删除refseToken
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tokey_key",tokenkey);
            hzhUserTokenMapper.delete(queryWrapper);
            //删除cookie
            CookieUtils.setUpCookie(getResponse(),Constants.User.TSFSE_TOKEN,"");
            //删除redis中的盐值
            redisUtils.delete(Constants.User.KEY_TOKEN + tokenkey);
            return R.SUCCESS("退出登录成功");
        }
    }

    //让用户强制下线
    public R logoutById(String userId) throws Exception {
/*        //可以通过userId 到refreshToken的表中查询到相关数据，然后退出登录
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
        }*/
        return null;
    }

    /**
     * 从数据库中找到refretoken  如果没有那就真的没有登录
     * 如果有，判断是否有过期
     * @param tokenKey
     * @return
     */
    private R chechResfreshToken(String tokenKey, String salt) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token_key",tokenKey);
        queryWrapper.select("refresh_token","user_id");
        HzhUserToken hzhUserRefreshToken = hzhUserTokenMapper.selectOne(queryWrapper);
        //查不到
        if (hzhUserRefreshToken != null){
            try {
                //如果没过期，通过用户ID查到用户内容，在创建token
                JwtUtil.parseJWT(hzhUserRefreshToken.getRefreshToken(), salt);
                String userId = hzhUserRefreshToken.getUserId();
                //先删除原来的数据 redis和数据库中的
                redisUtils.delete(Constants.User.KEY_TOKEN + tokenKey);

                //创建新的token
                HzhUser hzhUser = hzhUserMapper.selectById(userId);
                UserVo userVo = new UserVo();
                userVo.setId(userId);
                userVo.setUserName(hzhUser.getUserName());
                userVo.setSex(hzhUser.getSex());
                userVo.setAvatar(hzhUser.getAvatar());
                userVo.setPhonenumber(hzhUser.getPhonenumber());
                userVo.setStatus(hzhUser.getStatus());
                createToken(hzhUser);
                return R.SUCCESS("已登录",userVo);
            }catch (Exception e){
                e.printStackTrace();
                //过期就是未登录
            }
        }
        return R.NOT_LOGIN();
    }

    //登录
    @Override
    public R login(LoginVo loginUser, String verification) throws Exception {
        log.info("hzhUser ===> " + loginUser);
        log.info("verification ===> " + verification);

        // 登录不做  校验图灵验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(verification);
        //这种验证方式是会删除只能验证一次
        ResponseModel response = captchaService.verification(captchaVO);
        //String repCode = response.getRepCode();
        String repCode = "0000";
        //System.out.println("repCode ===> "+repCode);
        if(response.isSuccess() == false) {
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员

            //没有具体说明是什么异常
            if (repCode != null && repCode.equals(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getCode())) {
                return R.SUCCESS(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            } else if (repCode == null || !repCode.equals(RepCodeEnum.SUCCESS.getCode())) {
                return R.FAILED("图灵码验证失败");
            }

            //校验数据
            String userName = loginUser.getUserName();
            if (StringUtils.isEmpty(userName)) {
                return R.FAILED("账号不可以为空");
            }

            //对密码校验，前端传递来的密码不可能为明文传递通过MD5加密
            String password = loginUser.getPassword();
            if (StringUtils.isEmpty(password)) {
                return R.FAILED("密码不可以为空");
            }
            if (password.length() != 32) {
                return R.FAILED("请使用MD5摘要对密码进行转换");
            }

            //根据用户名、手机号、邮箱进行查询 用户
            HzhUser hasHzhUser = hzhUserMapper.selectOneByUserName(loginUser.getUserName());
            if (hasHzhUser == null) {
                return R.FAILED("账号不存在");
            } else if (hasHzhUser.getStatus().equals("1")) {
                return R.FAILED("账号已经被禁用");
            } else {
                //如果用户存在并且未被禁用
                //前端密码经过加密后再次经过passwordEncoder 加密后存入库中
                boolean matches = bCryptPasswordEncoder.matches(password, hasHzhUser.getPassword());
                if (matches) {
                    createToken(hasHzhUser);
                    UserVo userVo = new UserVo();
                    userVo.setAvatar(hasHzhUser.getAvatar());
                    userVo.setUserName(hasHzhUser.getUserName());
                    return R.SUCCESS("登录成功",userVo);
                } else {
                    return R.FAILED("用户名或密码错误");
                }
            }
        }
        return R.FAILED("图灵码验证失败");
    }

     /**
      * @Author Hou zhonghu
      * @Description  创建token
      *         -token              (有效期 2h  存入redis)
      *         -tokenKey           (Token 的MD5摘要值)
      *         -refreshToken       (有效期1个月，放到数据库中)
      *         UserVo 返回给前端不能有密码
      * @Date 2022/8/3 19:35
      * @Param HzhUser对象
      * @return token
      **/
    private void createToken(HzhUser hasUserByUserName) throws Exception {
        //删除当前用户的refreshToken,后面会重新创建
        QueryWrapper<HzhUserToken> tokenQueryWrapper = new QueryWrapper<>();
        tokenQueryWrapper.eq("user_id",hasUserByUserName.getId());
        hzhUserTokenMapper.delete(tokenQueryWrapper);

        Map<String, Object> claims = ClaimsUtils.user2Claims(hasUserByUserName);

        //创建token  JwtUtil.jwtTtl=== 1H  token入库
        String token = JwtUtil.createToken(claims, Constants.TimeSecond.TWO_HOUR, hasUserByUserName.getSalt());
        //返回给用户
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //一个月  refreshToken 入库
        String refreshToken = JwtUtil.createRefreshToken(hasUserByUserName.getId().toString(), Constants.Millions.MOUTH, hasUserByUserName.getSalt());

        //token 入redis  有效期2H   token 加前缀
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, 2, TimeUnit.HOURS);

        //返回给用户  tokenKey  cookie是固定的 不能根据登录的用户不同有不同的cookie name
        CookieUtils.setUpCookie(getResponse(),Constants.User.TSFSE_TOKEN,tokenKey);

        //refreshToken 入库
        HzhUserToken hzhRefreshToken = new HzhUserToken();
        hzhRefreshToken.setUserId(hasUserByUserName.getId().toString());
        hzhRefreshToken.setTokenKey(tokenKey);
        hzhRefreshToken.setRefreshToken(refreshToken);
        hzhRefreshToken.setCreateTime(date);

        HzhUserToken hzhUserToken = hzhUserTokenMapper.selectByUserId(hasUserByUserName.getId().toString());

//        if (hzhUserToken == null){
//            hzhUserTokenMapper.insert(hzhRefreshToken);
//        }else {
//            hzhUserTokenMapper.updateByUserId(hzhRefreshToken);
//        }
        if (hzhUserToken != null ){
            QueryWrapper<HzhUserToken> delQuery = new QueryWrapper<>();
            delQuery.eq("user_id",hasUserByUserName.getId());
            hzhUserTokenMapper.delete(delQuery);
        }
        hzhUserTokenMapper.insert(hzhRefreshToken);

        //将盐值保存到redis中，方便在解析token时获取
        String saltKey = redisKeyUtil.mkUserTokenSaltKey(tokenKey);
        //查看redis中有没有该用户的盐值
        Boolean aBoolean = redisUtils.hasKey(saltKey);
        if (aBoolean) {
            //有的话先删除后添加
            redisUtils.delete(saltKey);
        }
        //时间至少有2h  有效期一天
        redisUtils.set(saltKey, hasUserByUserName.getSalt(), 1, TimeUnit.DAYS);

    }

    //分页查询用户
    @Override
    public IPage<HzhUser> findAllByPage(Page<HzhUser> page) {
        QueryWrapper<HzhUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag",0);
        Page<HzhUser> hzhUserPage = hzhUserMapper.selectPage(page, queryWrapper);
        return hzhUserPage;
    }

    //根据用户ID查询用户
    @Override
    public HzhUser findByUserId(long id) {
        return hzhUserMapper.selectById(id);
    }

    //修改用户状态
    @Override
    public boolean updateByState(long id, String status,String updateDate) {
        boolean undateState = hzhUserMapper.updateStateById(id,status,updateDate);
        return undateState;
    }

    //根据用户ID删除用户
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
    public R reSetPasswordByAdmin(String id,ReSetPasswordVo reSetPasswordVo) throws Exception {
        String updateDate = DateUtils.getCurrent(DateUtils.dateFullPattern);

        HzhUser hzhUser = hzhUserMapper.selectById(id);
        if (hzhUser != null){
            if (StringUtils.isEmpty(reSetPasswordVo.getPassword()) || reSetPasswordVo.getPassword().length() != 32){
                return R.FAILED("密码校验出错");
            }else {
                String encode = bCryptPasswordEncoder.encode(reSetPasswordVo.getPassword());
                boolean b = hzhUserMapper.reSetPasswordByAdmin(Long.parseLong(id), updateDate, encode);
                if (b){
                    R r = logoutById(id);
                    return R.SUCCESS("密码重置成功",r);
                }else {
                    return R.FAILED("密码重置失败");
                }
            }
        }else {
            return R.FAILED("账号不存在");
        }
    }

    //用户自身修改密码
    @Override
    public R reSetPasswordBySelf(String mailCode, ReSetPasswordVo reSetPasswordVo) throws Exception {

        if (StringUtils.isEmpty(mailCode) || reSetPasswordVo == null){
            return R.FAILED("参数不可以为空");
        }else {
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email",reSetPasswordVo.getEmail());
            HzhUser hzhUser = hzhUserMapper.selectOne(queryWrapper);
            if (hzhUser != null){
                //对验证码进行比对
                String registerEmailRedisKey = redisKeyUtil.mkForgotCodeRedisKey(reSetPasswordVo.getEmail());
                String code = redisUtils.get(registerEmailRedisKey);
                //2022.08.13 17:59:19的找回密码得验证码：884726
                if (mailCode.equals(code.substring(29,35))){
                    String encode = bCryptPasswordEncoder.encode(reSetPasswordVo.getPassword());
                    boolean update = hzhUserMapper.updatePasswordByEmail(reSetPasswordVo.getEmail(),encode);
                    if (update){
                        //跟新成功以后退出登录
                        logout();
                        return R.SUCCESS("密码修改成功");
                    }else {
                        return R.FAILED("密码修改失败");
                    }
                }else {
                    return R.FAILED("验证码错误");
                }
            }else {
                return R.FAILED("该邮箱未查询到用户");
            }
        }
    }

    //用户注册
    @Override
    public R registerUser(String mailCode, HzhUser hzhUser) throws Exception {
        String currentdate = DateUtils.getCurrent(DateUtils.dateFullPattern);

        //判断邮箱是否以已经注册 是否为空
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(hzhUser.getEmail())){
            return R.FAILED("注册邮箱不可以为空");
        }
        //根据用户名、手机号、邮箱进行查询
        QueryWrapper<HzhUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", hzhUser.getUserName())
                .or().eq("email",hzhUser.getEmail())
                .or().eq("phonenumber",hzhUser.getPhonenumber());
        HzhUser hasHzhUser = hzhUserMapper.selectOne(queryWrapper);
        if (hasHzhUser != null && hasHzhUser.getEmail().equals(hzhUser.getEmail())){
            return R.FAILED("该邮箱已注册");
        }
        // 判断用户名是否被使用
        if (hasHzhUser != null && hasHzhUser.getUserName().equals(hzhUser.getUserName())){
            return R.FAILED("用户名已存在");
        }
        //对手机号进行判断
        boolean isPhone = FormatCheckUtils.isMobile(hzhUser.getPhonenumber());
        if (!isPhone ){
            return R.FAILED("手机号格式错误");
        }
        if (hasHzhUser != null){
            return R.FAILED("该手机号已注册");
        }
        //判断验证码是否正确
        String codeRedisKey = redisKeyUtil.mkRegisterCodeRedisKey(hzhUser.getEmail());
        String redisCode = redisUtils.get(codeRedisKey);
        if ( redisCode == null || !redisCode.substring(26,32).equals(mailCode)){
            return R.FAILED("验证码错误");
        }

        //对密码进行校验
        if (hzhUser.getPassword().length() != 32){
            return R.FAILED("请使用MD5摘要算法进行转换");
        }

        HzhUser hzhUserInsert = new HzhUser();
        hzhUserInsert.setUserName(hzhUser.getUserName());
        //密码加密
        hzhUserInsert.setPassword(bCryptPasswordEncoder.encode(hzhUser.getPassword()));
        hzhUserInsert.setUserDescription(Constants.UserDescription.MEMEBR_USER);
        hzhUserInsert.setStatus(Constants.User.UNFORBIDDENT_STATE);
        hzhUserInsert.setEmail(hzhUser.getEmail());
        hzhUserInsert.setPhonenumber(hzhUser.getPhonenumber());
        //TODO 前端不会做单选男女 默认为男
        hzhUserInsert.setSex("1");
        //设置默认头像
        hzhUserInsert.setAvatar("https://cdn.sunofbeaches.com/images/default_avatar.png");
        hzhUserInsert.setUserType(Constants.UserType.MEMEBR_USER);
        hzhUserInsert.setCreateTime(currentdate);
        hzhUserInsert.setUpdateBy(hzhUser.getUpdateBy());
        hzhUserInsert.setSalt(IdWorker.getIdStr());
        hzhUserInsert.setUpdateTime(currentdate);
        hzhUserInsert.setDelFlag(Constants.User.UNFORBIDDENT_STATE);

        int add = hzhUserMapper.insert(hzhUserInsert);
        if(add == 1){
            return R.SUCCESS("用户注册成功");
        }else {
            return R.FAILED("用户注册失败");
        }
    }

    //创建超级管理员
    @Override
    public R initAdminAccount(HzhUser hzhUser) {
        return null;
    }

    //根据条件查询并分页
    @Override
    public IPage<HzhUser> selectListByFilter(Page<HzhUser> page, HzhUser hzhUser) {
        //只查询未删除的用户
        QueryWrapper<HzhUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag",0)
                .or().like("user_name",hzhUser.getUserName())
                .or().eq("phonenumber",hzhUser.getPhonenumber())
                .or().eq("status",hzhUser.getStatus())
                .or().eq("user_type",hzhUser.getUserType())
                .or().eq("email",hzhUser.getEmail());
        Page<HzhUser> hzhUserPage = hzhUserMapper.selectPage(page, queryWrapper);
        return hzhUserPage;
    }


}
