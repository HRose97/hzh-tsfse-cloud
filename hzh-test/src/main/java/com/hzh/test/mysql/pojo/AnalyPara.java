package com.hzh.test.mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分析数据查询参数数据模型
 * @author 许华兴
 * @since  2020/11/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyPara {

    //系统标识
    public String strSystemCode;
    //节点名
    public String strHostCode;
    //监控项标识
    public String strItem;
    //监控项子序号
    public String strSubitem;
    //当前页
    public String strCurrentPage;
    //每页条数
    public String strPageSize;

    public AnalyPara(String strSystemCode, String strHostCode, String strItem, String strSubitem) {
        this.strSystemCode = strSystemCode;
        this.strHostCode = strHostCode;
        this.strItem = strItem;
        this.strSubitem = strSubitem;
    }
}
