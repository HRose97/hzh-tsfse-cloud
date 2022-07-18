package com.hzh.event;

import com.hzh.feign.clients.EventClient;
import com.hzh.feign.clients.OrderClient;
import com.hzh.feign.clients.TeamClinet;
import com.hzh.feign.clients.UserClient;
import com.hzh.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.hzh.**"})
@EnableFeignClients(clients = {OrderClient.class,EventClient.class, TeamClinet.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class HzhEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzhEventApplication.class, args);
    }

}
