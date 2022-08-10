package com.hzh.user.email;

import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.user.service.HzhUserExService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 邮箱相关请求控制类
 *      -- 发送邮箱验证码
 *      -- 检查邮箱是否有注册
 *      -- 检查用户名是否以及注册
 *
 *
 * 验证码发送和业务类型有关
 *     1. 注册验证码：  邮箱地址必须是未注册的
 *     2. 找回密码：    邮箱地址必须注册
 *     3. 更换邮箱地址： 新的邮箱地址必须是未注册
 * @author Hou Zhonghu
 * @since 2022/7/29 10:38
 */
@RestController
@RequestMapping("/email")
public class HzhUserExController {


    @Resource
    private HzhUserExService hzhUserExService;


     /**
      * @Author Hou zhonghu
      * @Description  发送邮箱
      * @Date 2022/7/29 10:41
      * @Param emailAddress  邮箱地址
      * @return
      **/
     @GetMapping("/register/emailCode")
     public ResultVO sendRegisterEmail(@RequestParam(value = "verification",required = false)String verification,
                                      @RequestParam("email") String emailAddress) throws Exception {
         ResultVO emailCode = hzhUserExService.sendEmailCode(verification,emailAddress);
         return  ResultVO.ok(emailCode);
     }

}
