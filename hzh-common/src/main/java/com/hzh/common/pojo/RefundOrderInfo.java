package com.hzh.common.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RefundOrderInfo {

  private Long refundId;
  private Long orderReturnId;
  private double refund;
  private String refundSn;
  private Long refundStatus;
  private Long refundChannel;
  private String refundContent;



}
