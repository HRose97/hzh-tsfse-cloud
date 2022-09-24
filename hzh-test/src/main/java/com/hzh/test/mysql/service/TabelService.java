package com.hzh.test.mysql.service;
import com.hzh.test.mysql.pojo.MoseSystemHost;
import com.hzh.test.mysql.pojo.vo.Messager;

/**
 * @author Hou Zhonghu
 * @since 2022/9/5 19:11
 */
public interface TabelService {

    void creatTable();

    void delTable();

    public Messager selHostInfoByHostCode(MoseSystemHost moseSystemHost) throws Exception;

    public Messager delHostCode(String strSystemCode, String strHostCode);
}
