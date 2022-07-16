package com.hzh.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应枚举
 * @author: Hou Zhonghu
 * @createTime: 2021/10/20
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS("AAAAAA", "操作成功"),
    INNER_EXCEPTION("EI0000", "系统内部异常！"),
    VALIDATE_ERROR("EV0001", "参数校验出错！"),
    SYSTEM_ERROR_NULL_HOST("ES0004","当前系统下没有主机"),

    SERVER_FEIGN_FUSING("SFF000", "服务调用发生熔断！"),


    //用户中心调度
    SERVER_FEING_USERCLIENT("UC000","用户中心调度发生熔断"),


    //订单中心调度
    SERVER_FEING_ORDERLIENT("OC000","订单中心调度发生熔断"),
    SERVER_FEING_ORDERNOSELECT("OC001","订单中心调度查询为空"),


    //球队中心调度
    SERVER_FEING_TEAMCLIENT("TC000","球队中心调度发生熔断"),


    //赛事中心调度
    SERVER_FEING_EVENTCLIENT("EC000","赛事中心调度发生熔断"),


    EXECUTOR_THREAD_POOL_REJECTED("ETPR00","执行器线程池拒绝线程入池！"),
    UPDATE_STATE_ERROR("EU0001","启用/停用更新失败！"),

    //参数为空
    PARAMETER_EMPTY("EP0001","参数为空");


    private String code;
    private String msg;

}
