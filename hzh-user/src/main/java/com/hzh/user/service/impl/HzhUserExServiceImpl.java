package com.hzh.user.service.impl;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.hzh.common.mapper.HzhUserMapper;
import com.hzh.common.pojo.vo.LoginUserVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.service.BaseService;
import com.hzh.common.utils.DateUtils;
import com.hzh.common.utils.FormatCheckUtils;
import com.hzh.common.utils.RedisKeyUtil;
import com.hzh.common.utils.RedisUtils;
import com.hzh.user.service.HzhUserExService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Hou Zhonghu
 * @since 2022/7/29 10:45
 */
@Service
@Slf4j
public class HzhUserExServiceImpl extends BaseService implements HzhUserExService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HzhUserMapper hzhUserMapper;

    @Resource
    private RedisKeyUtil redisKeyUtil;

    @Resource
    private CaptchaService captchaService;

     /**
      * @Author Hou zhonghu
      * @Description  发送邮箱验证码
      * @Date 2022/7/29 10:49
      * @Param emailAddress 邮箱地址
      * @Param mustRegister 邮箱地址是否以及注册
      * @return 
      **/
    @Override
    public ResultVO sendEmailCode(String verfication,String emailAddress) throws Exception {
        String date = DateUtils.getCurrent(DateUtils.ticketPattern);
        log.info("String verfication ===> " +  verfication);

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(verfication);
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
                return ResultVO.ok(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            }else if(repCode == null || !repCode.equals(RepCodeEnum.SUCCESS.getCode())){
                return ResultVO.ok("图灵码验证失败");
            }
        }

        log.info("email address {},",emailAddress);
        //检查数据是否正确
        if (TextUtils.isEmpty(emailAddress)) {
            //不可以为空
            return ResultVO.ok("邮箱地址不可以为空");
        }
        //对邮箱地址校验
        boolean isEmailTrue = FormatCheckUtils.isEmail(emailAddress);
        if (!isEmailTrue){
            //邮箱格式不正确
            return ResultVO.ok("邮箱格式不正确，请检查邮箱格式");
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
                return ResultVO.ok("请不要通过此ip"+remoteAddr+"频繁发送");
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
                return ResultVO.ok("请不要通过此邮箱账号"+emailAddress+"频繁发送");
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
        //发送验证码
        //EmailSender.sendEmailSendCode(emailAddress, "注册验证码："+ mailCode +",5分钟内有效！");

        return ResultVO.ok("邮箱验证发送成功");
    }

}
