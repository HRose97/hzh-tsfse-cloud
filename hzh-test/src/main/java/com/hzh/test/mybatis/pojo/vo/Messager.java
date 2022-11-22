package com.hzh.test.mybatis.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Messager implements Serializable {
    //执行状态
    public String strState;
    //返回信息
    public String strMsg;
    //执行结果
    public Object strResult;

}
