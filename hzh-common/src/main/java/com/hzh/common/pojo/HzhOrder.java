package com.hzh.common.pojo;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HzhOrder implements Serializable {



  public static final long serialVersionUID=1L;

  private String orderId;
  private long userId;
  private long couponId;
  private String createTime;
  private String memberUsername;
  private double totalAmount;
  private double payAmount;
  private double promotionAmount;
  private double integrationAmount;
  private double couponAmount;
  private double discountAmount;
  private String payType;
  private String status;
  private long integration;
  private String billType;
  private String billHeader;
  private String billContent;
  private String billReceiverPhone;
  private String billReceiverEmail;
  private String note;
  private long useIntegration;
  private String paymentTime;
  private String orderType;
  private String orderState;


}
