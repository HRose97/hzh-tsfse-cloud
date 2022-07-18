package com.hzh.common.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RefundOrderInfo {

  private long refundId;
  private long orderReturnId;
  private double refund;
  private String refundSn;
  private long refundStatus;
  private long refundChannel;
  private String refundContent;



}
