package com.hzh.common.respone;

import lombok.Data;

/**
 * 统一结果返回类
 *
 * 数据有：
 *      - 是否成功：success[true/false] 类型 boolean
 *      - 状态码： code[20000/40000]  类型int
 *      - 消息： msg对code进行说明，比如说： 操作成功；操作失败；登录成功... 类型 Strin
 *      - 返回的数据： data 类型Object
 *
 * @author Hou Zhonghu
 * @since 2022/8/11 14:25
 */
@Data
public class R {

    public static final int CODE_SUCCESS = 20000;
    public static final int CODE_FAILED = 40000;
    public static final int CODE_NOT_LOGIN = 40001;

    //是否成功
    private boolean success;
    //状态码
    private int code;
    //描述
    private String msg;
    //数据
    private Object data;


    //提供一些静态的方法，可以快速的创建返回对象
    public static R SUCCESS(String msg){
        R r = new R();
        r.code = CODE_SUCCESS;
        r.msg = msg;
        r.success = true;
        return r;
    }

    public static R SUCCESS(String msg,Object data){
        R success = SUCCESS(msg);
        success.data = data;
        return success;
    }

    public static R NOT_LOGIN(){
        R filed = FAILED("账号未登录");
        filed.code = CODE_NOT_LOGIN;
        return filed;
    }

    public static R FAILED(String msg){
        R r = new R();
        r.code = CODE_FAILED;
        r.msg = msg;
        r.success = false;
        return r;
    }


    public static R FAILED(String msg, Object data){
        R success = SUCCESS(msg);
        success.data = data;
        return success;
    }

}
