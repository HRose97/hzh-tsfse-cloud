package com.hzh.user;

import com.hzh.feign.clients.EventClient;
import com.hzh.feign.clients.OrderClient;
import com.hzh.feign.clients.TeamClinet;
import com.hzh.feign.config.DefaultFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableFeignClients(clients = {OrderClient.class,EventClient.class, TeamClinet.class}, defaultConfiguration = DefaultFeignConfiguration.class)
@SpringBootApplication(scanBasePackages ={"com.hzh"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
