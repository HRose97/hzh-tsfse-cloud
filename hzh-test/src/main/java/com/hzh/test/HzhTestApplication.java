package com.hzh.test;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.hzh.test.mysql.mapper")
public class HzhTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzhTestApplication.class, args);
    }

}
