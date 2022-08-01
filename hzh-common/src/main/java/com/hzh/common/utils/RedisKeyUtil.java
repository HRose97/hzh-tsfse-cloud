package com.hzh.common.utils;

import com.hzh.common.pojo.CoachInfo;
import com.hzh.common.pojo.EventInfo;
import com.hzh.common.pojo.HzhOrder;
import com.hzh.common.pojo.HzhUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisKeyUtil {

    /**
     * 生成订单项键名
     * @param
     * @return
     *
     * Order:羽毛球:YMQ-2-202207141418:2
     *
     */
    public String mkOrderRedisKey(HzhOrder orderInfo) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("Order");
        stringBuffer.append(":")
                .append(orderInfo.getOrderType())
                .append(":")
                .append(orderInfo.getUserId())
                .append(":")
                .append(orderInfo.getOrderId());
        return stringBuffer.toString().trim();
    }

    /**
     * 生成赛事项键名
     * @param
     * @return
     *
     * Event:羽毛球:1000:2022-07-21 15:50:00
     * Event:羽毛球:中国:北京:东单:1000
     *
     */
    public String mkEventRedisKey(EventInfo eventInfo) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("Event");
        stringBuffer.append(":")
                .append(eventInfo.getPhysicalName())
                .append(":")
                .append(eventInfo.getPhysicalId())
                .append(":")
                .append(eventInfo.getHeldCountry())
                .append(":")
                .append(eventInfo.getHeldLocation())
                .append(":")
                .append(eventInfo.getHeldVenues())
                .append(":")
                .append(eventInfo.getMaximumCapacity());
        return stringBuffer.toString().trim();
    }


    /**
     * 生成球队项键名
     * @param
     * @return
     */
    public String mkTeamRedisKey(CoachInfo coachInfo) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("Team");
        stringBuffer.append(":")
                .append("Coach")
                .append(":")
                .append(coachInfo.getCoachType())
                .append(":")
                .append(coachInfo.getCoachTeamId());
        return stringBuffer.toString().trim();
    }

    /**
     * 生成用户中心项键名
     * @param
     * @return
     */
    public String mkUserRedisKey(HzhUser hzhUser) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("User");
        stringBuffer.append(":")
                .append(hzhUser.getUserType())
                .append(":")
                .append(hzhUser.getId())
                .append(":")
                .append(hzhUser.getPhonenumber());
        return stringBuffer.toString().trim();
    }


    /**
     * 生成用户注册ip相关键名
     * @param
     * @return
     */
    public String mkRegisterIPRedisKey(String  remoteAddr) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("User");
        stringBuffer.append(":")
                .append("Register")
                .append(":")
                .append(remoteAddr);
        return stringBuffer.toString().trim();
    }


    /**
     * 生成用户注册邮箱相关键名
     * @param
     * @return
     */
    public String mkRegisterEmailRedisKey(String  emailAddress) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("User");
        stringBuffer.append(":")
                .append("Register")
                .append(":")
                .append(emailAddress);
        return stringBuffer.toString().trim();
    }


    /**
     * 生成用户注册验证码键名
     * @param
     * @return
     */
    public String mkRegisterCodeRedisKey(String  code,String emailAddress) throws Exception{
        StringBuffer stringBuffer = new StringBuffer("User");
        stringBuffer.append(":")
                .append("Register")
                .append(":")
                .append(emailAddress)
                .append(":")
                .append(code);
        return stringBuffer.toString().trim();
    }

}