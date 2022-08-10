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
    SUCCESS_ERROR("BBBBBB","业务异常"),

    INNER_EXCEPTION("EI0000", "系统内部异常！"),
    VALIDATE_ERROR("EV0001", "参数校验出错！"),
    PASSWORD_MD5_ERROR("MD0001", "请使用MD5摘要算法对密码进行转换！"),
    PASSWORD_ERROR("MD0002", "密码校验出错！"),

    //登录相关
    LOGIN_ERROR("LE0001","用户名或密码错误"),
    LOGIN_ACCOUNT_ERROR("LE0002","账号不存在"),
    NOT_LOGIN_USER("LE0003","用户未登录"),
    LOGIN_DISABLED_USER("LE0002","登录用户已被禁用"),

    UPDATE_PASSWORD_ERROR("EP0001","密码修改失败"),

    //注册相关
    REGUST_EMAIL_ISEXIST("ES0001","该邮箱已经注册"),
    REGUST_EMAIL_NOTISEXIST("ES0002","该邮箱未注册"),


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

    //验证码相关
    //repCode  0000  无异常，代表成功
    //repCode  9999  服务器内部异常
    //repCode  0011  参数不能为空
    //repCode  6110  验证码已失效，请重新获取
    //repCode  6111  验证失败
    //repCode  6112  获取验证码失败,请联系管理员
    CODE_ERROR("EV0002", "验证码校验出错！"),
    VERIFICATION_CODE_ERROR1("VC9999","服务器内部异常"),
    VERIFICATION_CODE_ERROR2("VC0011","参数不能为空"),
    VERIFICATION_CODE_ERROR3("VC6110","验证码已失效,请重新获取"),
    VERIFICATION_CODE_ERROR4("VC6111","验证失败"),
    VERIFICATION_CODE_ERROR5("VC6112","获取验证码失败,请联系管理员"),



    //参数为空
    PARAMETER_EMPTY("EP0001","参数为空");



    private String code;
    private String msg;

}
