package com.hzh.order;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Hou Zhonghu
 * @since 2022/7/27 9:54
 */
@SpringBootTest
public class UserApplicationTest {


    //spring security 密码加密
    @Test
    public void contextLoads(){
        PasswordEncoder pw = new BCryptPasswordEncoder();
        //加密
        String encode = pw.encode("1234");
        System.out.println(encode);

        System.out.println("==================================");
        //密码匹配
        boolean matches = pw.matches("1234", encode);
        System.out.println(matches);
    }

}
