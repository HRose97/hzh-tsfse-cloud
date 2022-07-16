package com.hzh.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * java代码实现日志配置
 *
 * 类上不加注解不会生效
 *      具体注解加载方式：
 *          1.加载具体的client上   @FeignClient("服务名",configuration)  就只针对当前服务生效
 *
 *          2.启动类上@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration.class) 全局有效
 *
 * @author Hou Zhonghu
 * @since 2022/6/25 15:49
 */
public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.BASIC;
    }
}
