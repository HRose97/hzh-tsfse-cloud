package com.hzh.user.service.impl;

import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.utils.RedisKeyUtil;
import com.hzh.common.utils.RedisUtils;
import com.hzh.user.service.HzhUserExService;
import com.hzh.user.utils.EmailSender;
import com.hzh.user.utils.FormatCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
public class HzhUserExServiceImpl implements HzhUserExService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisKeyUtil redisKeyUtil;

     /**
      * @Author Hou zhonghu
      * @Description  发送邮箱验证码
      * @Date 2022/7/29 10:49
      * @Param emailAddress 邮箱地址
      * @Param mustRegister 邮箱地址是否以及注册
      * @return 
      **/
    @Override
    public ResultVO sendEmailCode(String emailAddress,boolean mustRegister) throws Exception {
        log.info("email address {} must register {},",emailAddress,mustRegister);
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
        //根据标记检查邮箱是否正确  此时邮箱地址必须为未注册
        if (mustRegister){
            //TODO
        }
        //TODO 防止恶意频繁调用  可以通过IP，通过邮箱地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        System.out.println("请求中截取内容："+request);

        //ip地址
        String remoteAddr = request.getRemoteAddr();
        remoteAddr = remoteAddr.replaceAll(":", "-");
        log.info("remote address ...{}",remoteAddr);

        //通过ip地址判断是否频繁发送
        String registerIpRedisKey = redisKeyUtil.mkRegisterIPRedisKey(remoteAddr);
        String ipKeyTimes = redisUtils.get(registerIpRedisKey);
        if (!StringUtils.isEmpty(ipKeyTimes)){
            int i = Integer.parseInt(ipKeyTimes);
            log.info("当前ip{}调用了{}次",remoteAddr,i);
            if (i > 10){
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
            if (i > 10){
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
        String codeRedisKey = redisKeyUtil.mkRegisterCodeRedisKey(String.valueOf(mailCode), emailAddress);
        redisUtils.set(codeRedisKey, String.valueOf(mailCode),5,TimeUnit.MINUTES);
        //发送验证码
        //EmailSender.sendEmailSendCode(emailAddress, "注册验证码："+ mailCode +",5分钟内有效！");

        return ResultVO.ok("邮箱验证发送成功");
    }
}
