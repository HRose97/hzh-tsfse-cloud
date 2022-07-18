package com.hzh.team;


import com.hzh.feign.clients.EventClient;
import com.hzh.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages ={"com.hzh"})
@EnableFeignClients(clients = EventClient.class,defaultConfiguration = DefaultFeignConfiguration.class)
public class HzhTeamApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzhTeamApplication.class, args);
    }

}
