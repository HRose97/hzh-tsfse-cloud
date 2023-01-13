package com.hzh.team;


import com.hzh.common.utils.DateUtils;
import com.hzh.feign.clients.EventClient;
import com.hzh.feign.config.DefaultFeignConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Date;


@SpringBootApplication(scanBasePackages ={"com.hzh"})
@Slf4j
@EnableFeignClients(clients = EventClient.class,defaultConfiguration = DefaultFeignConfiguration.class)
public class HzhTeamApplication {

    public static void main(String[] args) {
        String date = DateUtils.getCurrent(DateUtils.ticketPattern);
        log.info("北京时间----" +  date + "            球队中心正在启动..............");
        SpringApplication.run(HzhTeamApplication.class, args);
        log.info("北京时间----" +  date + "            球队中心启动完成..............");
    }

}
