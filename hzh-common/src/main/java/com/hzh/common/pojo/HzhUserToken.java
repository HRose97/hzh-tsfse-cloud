package com.hzh.common.pojo;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HzhUserToken {

  private String id;
  private String userId;
  private String refreshToken;
  private String tokenKey;
  private String loginFrom;
  private String appId;
  private String createTime;
  private String updateTime;


}
