package com.hzh.test.mysql.mapper;

import com.hzh.test.mysql.pojo.MoseSystemHost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

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
