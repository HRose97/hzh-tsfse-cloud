package com.hzh.order.service.impl;

import com.hzh.common.mapper.PaymentInfoMapper;
import com.hzh.common.pojo.CoachInfo;
import com.hzh.common.pojo.PaymentInfo;
import com.hzh.order.service.PaymentInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 17:07
 */
@Service
public class PaymentInfoImpl implements PaymentInfoService {

    @Resource
    private PaymentInfoMapper paymentInfoMapper;


    @Override
    public PaymentInfo selectByOrderId(String orderId) {
        return paymentInfoMapper.selectById(orderId);
    }
}
