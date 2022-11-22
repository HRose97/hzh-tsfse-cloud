package com.hzh.test.mybatis.mapper;

import com.hzh.test.mybatis.pojo.MoseSystemHost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hou Zhonghu
 * @since 2022/9/5 19:12
 */
@Mapper
public interface TabelMapper {

    public int creatTable(@Param("tableName") String tableName);

    public int delTable(@Param("tableName") String tableName);

    public MoseSystemHost selHostInfoByHostCode(String strHostCode);

    public int insMoseSystemHost(MoseSystemHost moseSystemHost);

    public int delHostInfoByHostCode(@Param("strHostCode") String strHostCode,
                              @Param("strSystemCode") String strSystemCode);
}
