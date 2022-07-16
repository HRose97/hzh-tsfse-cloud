package com.hzh.order;

import com.hzh.common.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author Hou Zhonghu
 * @since 2022/7/14 15:16
 */
@SpringBootTest
public class RedisTest {


    @Resource
    private  RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    RedisUtils redisUtils;

    @Test
    public void test(){
        stringRedisTemplate.opsForValue().set("HHH","测试添加");


/*        String key = "Hzh";
        Object hzh = stringRedisTemplate.opsForValue().get(key);
        System.out.println(hzh);*/
        redisUtils.set("TTT","测试工具类");

    }


}

