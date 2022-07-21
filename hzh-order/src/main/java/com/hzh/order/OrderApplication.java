package com.hzh.order;

import com.hzh.feign.clients.UserClient;
import com.hzh.feign.config.DefaultFeignConfiguration;
import com.hzh.order.mq.ProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

//@MapperScan("com.hzh.common.mapper")
@SpringBootApplication(scanBasePackages ={"com.hzh.**"})
//@EnableFeignClients //自动装配开关  缺少clients = UserClient.class 会导致spring中没有UserClient对象，即UserClient没有实例化
//clients = UserClient.class 可以为数组clients = {UserClient.class}
@EnableFeignClients(clients = UserClient.class,defaultConfiguration = DefaultFeignConfiguration.class)
public class OrderApplication {

    public static void main(String[] args) {
        //SpringApplication.run(OrderApplication.class, args);


        ConfigurableApplicationContext run = SpringApplication.run(OrderApplication.class, args);
        ProducerService producerService = (ProducerService)run.getBean("ProducerService");
        //方法在ProducerSerice中
        //简单消息
        //producerService.sentMsg();

        //同步消息
        producerService.sendSyncMessage();

        //发送异步消息
        //producerService.sendAsynMessage();

        //发送单向消息
        //producerService.sendOneWay();

        //发送同步按顺序消息
        //producerService.syncSendOrderly();

        //发送事务消息
        //producerService.sendTransactionMessage();

        //发送过滤消息
        //producerService.sendMessageWithTag();

        //发送SQL表达式头信息消息，测试SQL表达式过滤消息
        //producerService.sendMessageWithSQL();
    }

}