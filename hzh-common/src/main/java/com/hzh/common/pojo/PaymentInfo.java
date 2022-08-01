package com.hzh.common.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaymentInfo {

  private Long id;
  private String orderSn;
  private Long orderId;
  private String alipayTradeNo;
  private double totalAmount;
  private String subject;
  private String paymentStatus;
  private String createTime;
  private String confirmTime;
  private String callbackContent;
  private String callbackTime;

}
