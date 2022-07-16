package com.hzh.order;

import com.hzh.feign.clients.UserClient;
import com.hzh.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//@MapperScan("com.hzh.common.mapper")
@SpringBootApplication(scanBasePackages ={"com.hzh"})
//@EnableFeignClients //自动装配开关  缺少clients = UserClient.class 会导致spring中没有UserClient对象，即UserClient没有实例化
//clients = UserClient.class 可以为数组clients = {UserClient.class}
@EnableFeignClients(clients = UserClient.class,defaultConfiguration = DefaultFeignConfiguration.class)
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}