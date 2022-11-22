package com.hzh.test.mybatis.service.impl;

import com.hzh.test.mybatis.mapper.TabelMapper;
import com.hzh.test.mybatis.pojo.MoseSystemHost;
import com.hzh.test.mybatis.pojo.vo.Messager;
import com.hzh.test.mybatis.service.TabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/9/5 19:11
 */
@Slf4j
@Service
@EnableTransactionManagement
public class TabelServiceImpl implements TabelService {

    @Resource
    public TabelMapper tabelMapper;

    @Override
    public void creatTable() {
        tabelMapper.creatTable("mose_table");

    }

    @Override
    public void delTable() {
        tabelMapper.delTable("mose_table");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Messager selHostInfoByHostCode(MoseSystemHost moseSystemHost){
        boolean bResult = false;
        Messager messager = new Messager();
        MoseSystemHost moseSystemHostInfo = tabelMapper.selHostInfoByHostCode(moseSystemHost.getStrHostCode());
        try {
            if (moseSystemHostInfo == null) {
                int result = tabelMapper.insMoseSystemHost(moseSystemHost);
                if (result > 0) {
                    String tableName = moseSystemHost.getStrSystemCode() + "_" + moseSystemHost.getStrHostCode() + "_notice_data";
                    tabelMapper.delTable(tableName);
                    tabelMapper.creatTable(tableName);
                    //测试事务回滚
//                    int i = 1/0;
                    bResult = true;
                }else {
                    messager.setStrMsg("应用系统主机信息添加操作失败");
                    messager.setStrState("0");
                }
            }
        }catch (Exception e){
            messager.setStrState("-1");
            messager.setStrMsg("应用系统主机信息添加操作失败");
            //throw new RuntimeException(e);
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        if (bResult) {
            messager.setStrMsg("应用系统主机信息添加操作完成");
            messager.setStrState("1");
        } else {
            messager.setStrMsg("应用系统主机信息添加操作失败");
            messager.setStrState("0");
        }
        log.info("========应用系统主机信息添加完成");
        return messager;
    }

    @Override
    @Transactional
    public Messager delHostCode(String strSystemCode, String strHostCode) {
        boolean bResult = false;
        Messager messager = new Messager();
        if (strHostCode != null && !strHostCode.equals("") && strSystemCode != null & !strSystemCode.equals("")){
            int delInfo = tabelMapper.delHostInfoByHostCode(strHostCode,strSystemCode);
            if (delInfo == 1 ){
                String tableName = strSystemCode + "_" + strHostCode + "_notice_data";
                tabelMapper.delTable(tableName);
                bResult = true;
            }
        }
        if (bResult){
            messager.setStrMsg("应用系统主机信息删除操作完成");
            messager.setStrState("1");
        }else {
            messager.setStrMsg("应用系统主机信息删除操作失败");
            messager.setStrState("0");
        }
        return messager;
    }





}
