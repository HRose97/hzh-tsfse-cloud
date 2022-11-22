package com.hzh.test.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 应用系统主机信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoseSystemHost implements Serializable {
    //系统标识
    public String strSystemCode;
    //节点标识
    public String strHostCode;
    //节点中文名
    public String strHostName;
    //主机说明
    public String strHostComment;
    //节点地址
    public String strHostIP;
    //节点存放地
    public String strHostArea;
    //是否有效
    public String strHostState;
    //节点类型
    public String strHostType;
    //端口
    public String port;
    //健康度
    public String strHealth;


}
