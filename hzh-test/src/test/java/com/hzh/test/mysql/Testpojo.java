package com.hzh.test.mysql;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 测试根据实体类生成mysql表 的实体类
 *
 * @author Hou Zhonghu
 * @since 2022/8/31 14:58
 */
@Data
@ToString(callSuper = true)   //解决toString方法缺少父类属性的问题
@NoArgsConstructor
public class Testpojo {
    //记录序号
    public Long lNoticeSequence;
    //信息日期
    public String strNoticeDate;
    //信息时间
    public String strNoticeTime;

    //采集项标识
    public String strItem;

    //采集项名称
    public String strItemName;

    //采集子项项目序号
    public String strSubitem;

    //信息级别
    public String strLevel;

    //信息数据
    public String strNoticeData;

    //信息处理状态
    public String strNoticeState;

    //处理人
    public String strHandler;

    //处理日期时间
    public String strHandlerDatetime;

    //处理简要描述
    public String strHandlerDescribe;

    //系统标识
    public String strSystemCode;

    //系统中文名
    public String strSystemName;

    //主机标识
    public String strHostCode;

    //主机中文名
    public String strHostName;

}
