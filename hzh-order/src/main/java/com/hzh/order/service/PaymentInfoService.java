package com.hzh.order.service;

import com.hzh.common.pojo.CoachInfo;
import com.hzh.common.pojo.PaymentInfo;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 17:07
 */
public interface PaymentInfoService {

    PaymentInfo selectByOrderId(String orderId);

}
