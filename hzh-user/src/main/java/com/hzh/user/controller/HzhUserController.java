package com.hzh.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.LoginVo;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.respone.R;
import com.hzh.common.utils.*;
import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;



/**
 *
 * 用户   前端控制器
 * @author Hou Zhonghu
 * @since 2022/7/12 14:50
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class HzhUserController {

    @Resource
    private HzhUserService hzhUserService;

    @GetMapping("/test")
    public String test(){
        return "测试";
    }

    /**
     * @Author Hou zhonghu
     * @Description  用户注册发送邮箱
     * @Date 2022/7/29 10:41
     * @Param emailAddress  邮箱地址
     * @Param mustRegister 邮箱是否注册
     * @return
     **/
    @GetMapping("/register/emailCode")
    public R sendRegisterEmail(@RequestParam(value = "verification",required = false)String verification,
                               @RequestParam("email") String emailAddress) throws Exception {
        return hzhUserService.sendEmailCode(verification,emailAddress,false);

    }

    /**
     * @Author Hou zhonghu
     * @Description  发送邮箱
     * @Date 2022/7/29 10:41
     * @Param emailAddress  邮箱地址
     * @Param mustRegister 邮箱是否注册
     * @return
     **/
    @GetMapping("/forget/emailCode")
    public R sendReSetPasswordEmail(@RequestParam(value = "verification",required = false)String verification,
                               @RequestParam("email") String emailAddress) throws Exception {
        return hzhUserService.sendReSetPasswordEmail(verification,emailAddress,false);

    }

    /**
     *  用户注册
     * @param mailCode
     * @param hzhUser
     * @return
     * @throws Exception
     */
    @PostMapping("/user/register")
    public R registerUser(@RequestParam(value = "mailCode",required = false)String mailCode, @RequestBody HzhUser hzhUser) throws Exception {
        return hzhUserService.registerUser(mailCode,hzhUser);
    }


     /**
      * @Author Hou zhonghu
      * @Description  用户登录
      * @Date 2022/8/3 14:14
      * @Param emial/pohnenum/username
      * @return 
      **/
     @PostMapping("/user/login")
      public R userLogin(@RequestBody LoginVo loginVo, @RequestParam("verification")String  verification) throws Exception {
         return hzhUserService.login(loginVo,verification);
     }

     //解析Token
     @GetMapping("/user/checkToken")
     public R checkToken() throws Exception {
         return hzhUserService.chechToken();
     }

     //退出登录
     @GetMapping("/user/logout")
     public R loginOut() throws Exception {
         return hzhUserService.logout();
     }

    //修改密码   用户自己修改
    @PutMapping("/user/reSetPasswordBySelf")
    public R reRendEmail(@RequestParam(value = "mailCode",required = false)String mailCode,
                         @RequestBody ReSetPasswordVo reSetPasswordVo) throws Exception {
        return  hzhUserService.reSetPasswordBySelf(mailCode,reSetPasswordVo);
    }

}
