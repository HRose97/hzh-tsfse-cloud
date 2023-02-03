package com.hzh.test;

import com.hzh.common.utils.DateUtils;
import com.hzh.common.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
//@MapperScan("com.hzh.test.mysql.mapper")
@SpringBootApplication(scanBasePackages ={"com.hzh"})

@Slf4j
public class HzhTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzhTestApplication.class, args);

    }

}
