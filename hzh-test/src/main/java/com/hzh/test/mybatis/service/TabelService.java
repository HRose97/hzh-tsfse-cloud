package com.hzh.test.mybatis.service;
import com.hzh.test.mybatis.pojo.MoseSystemHost;
import com.hzh.test.mybatis.pojo.vo.Messager;

/**
 * @author Hou Zhonghu
 * @since 2022/9/5 19:11
 */
public interface TabelService {

    void creatTable();

    void delTable();

    Messager selHostInfoByHostCode(MoseSystemHost moseSystemHost) throws Exception;

    Messager delHostCode(String strSystemCode, String strHostCode);
}
