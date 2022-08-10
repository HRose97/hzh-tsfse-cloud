package com.hzh.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.utils.*;
import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;


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

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisKeyUtil redisKeyUtil;

    @Resource
    private PasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test")
    public String test(){
        return "测试";
    }

    @PostMapping("/user/register")
    public ResultVO register(@RequestParam(value = "mailCode",required = false)String mailCode,@RequestBody HzhUser hzhUser) throws Exception {

        //TODO 前端post传递参数为空，为了继续学习，直接返回字符串
        System.out.println(hzhUser);
        HashMap<String,Object> result = new HashMap<>();
        String currentdate = DateUtils.getCurrent(DateUtils.dateFullPattern);

        // TODO 判断验证码是否正确
        String codeRedisKey = redisKeyUtil.mkRegisterCodeRedisKey(hzhUser.getEmail());
        String redisCode = redisUtils.get(codeRedisKey);
        if ( redisCode == null || !redisCode.substring(26,32).equals(mailCode)){
            return ResultVO.ok("验证码错误");
        }
        // 判断用户名是否被使用
        HzhUser hasUsername = hzhUserService.findByUserName(hzhUser.getUserName());
        if (hasUsername != null && hasUsername.getUserName().equals(hzhUser.getUserName())){
            return ResultVO.ok("用户名已存在");
        }
        //判断邮箱是否以已经注册
        HzhUser hasEmail = hzhUserService.findByEmail(hzhUser.getEmail());
        if (hasEmail != null && hasEmail.getEmail().equals(hzhUser.getEmail())){
            return ResultVO.ok("该邮箱已注册");
        }

        boolean isPhone = FormatCheckUtils.isMobile(hzhUser.getPhonenumber());
        if (!isPhone ){
            return ResultVO.ok("手机号格式错误");
        }
        HzhUser hzhPhone = hzhUserService.findByPhoneNum(hzhUser.getPhonenumber());
        if (hzhPhone != null){
            return ResultVO.ok("该手机号已注册");
        }
        //TODO 暂时不对密码强度校验
/*        boolean b = FormatCheckUtils.checkPasswordRule(hzhUser.getPassword(),hzhUser.getUserName());
        if (hzhUser.getPassword().length() < 8  ){
            return ResultVO.ok("请至少输入8位数密码");
        }if (!b){
            return ResultVO.ok("密码不符和规则，请输入包括大小写字母、数字、特殊符号中的3种");
        }*/

        //密码加密
        //String encode = bCryptPasswordEncoder.encode(hzhUser.getPassword());

        HzhUser hzhUserInsert = new HzhUser();
        hzhUserInsert.setUserName(hzhUser.getUserName());
        hzhUserInsert.setPassword(bCryptPasswordEncoder.encode(hzhUser.getPassword()));
        hzhUserInsert.setUserDescription(Constants.UserDescription.MEMEBR_USER);
        hzhUserInsert.setStatus(Constants.User.UNFORBIDDENT_STATE);
        hzhUserInsert.setEmail(hzhUser.getEmail());
        hzhUserInsert.setPhonenumber(hzhUser.getPhonenumber());
        //TODO 前端不会做单选男女 默认为男
        hzhUserInsert.setSex("1");
        //设置默认头像
        hzhUserInsert.setAvatar("https://www.manpingou.com/uploads/allimg/170221/25-1F221135231E5.jpg");
        hzhUserInsert.setUserType(Constants.UserType.MEMEBR_USER);
        hzhUserInsert.setCreateTime(currentdate);
        hzhUserInsert.setUpdateBy(hzhUser.getUpdateBy());
        hzhUserInsert.setSalt(IdWorker.getIdStr());
        hzhUserInsert.setUpdateTime(currentdate);
        hzhUserInsert.setDelFlag(Constants.User.UNFORBIDDENT_STATE);

        int add = hzhUserService.addUser(hzhUserInsert);
        if(add == 1){
            result.put("res","1");
            result.put("msg","用户注册成功");
            return ResultVO.ok(result);
        }else {
            result.put("res","0");
            result.put("msg","用户注册失败");
            return ResultVO.ok(result);
        }

    }


     /**
      * @Author Hou zhonghu
      * @Description  用户登录
      * @Date 2022/8/3 14:14
      * @Param emial/pohnenum/username
      * @return 
      **/
     @PostMapping("/user/login")
      public ResultVO userLogin(@RequestBody HzhUser loginUser,@RequestParam("verification")String  verification) throws Exception {
         ResultVO loginflag = hzhUserService.login(loginUser,verification);
         if (loginflag.getCode().equals("AAAAAA")){
             return ResultVO.ok("登录成功");
         }else {
             return ResultVO.okAndError(loginflag.getMsg());
         }
     }
     //解析Token
     @GetMapping("/user/getToken")
     public ResultVO checkToken()throws Exception {
         return hzhUserService.chechToken();
     }

     //退出登录
     @GetMapping("/user/logout")
     public ResultVO loginOut() throws Exception {
         return hzhUserService.logout();
     }

    //修改密码   用户自己修改
    @PutMapping("/user/reSetPassword")
    public ResultVO reRendEmail(@RequestParam(value = "mailCode",required = false)String mailCode,
                                @RequestBody ReSetPasswordVo reSetPasswordVo) throws Exception {
        return  hzhUserService.reSetPassword(mailCode,reSetPasswordVo);
    }

    //重置密码  管理员重置
    @GetMapping("/user/reSetPasswordByAdmin")
    public ResultVO reSetPasswordByAdmin(@RequestParam(value = "id")String id,
                                         @RequestBody ReSetPasswordVo reSetPasswordVo) throws Exception {
        return hzhUserService.reSetPasswordByAdmin(id,reSetPasswordVo);
    }


    //分页查询
    @GetMapping("/user/getAllUserByPage")
    public ResultVO getAllUserByPage(@RequestParam("current")String current,@RequestParam("size")String size){
        if ( !StringUtils.isEmpty(current) && !StringUtils.isEmpty(current) ){
            Page<HzhUser> page = new Page<>(Long.parseLong(current), Long.parseLong(size));
            IPage<HzhUser> hzhUserIPage = hzhUserService.findAllByPage(page);
            return ResultVO.ok(hzhUserIPage);
        }else {
            return null;
        }
    }

    //禁用用户
    @PutMapping("/user/disableUserById")
    public ResultVO disableUserById(@RequestParam("id")String id,@RequestParam("status")String status){
        String updateDate = DateUtils.getCurrent(DateUtils.dateFullPattern);
        if ( !StringUtils.isEmpty(id) ){
             HzhUser findByfilter = hzhUserService.findByFitler(Long.parseLong(id));
            if (findByfilter != null && findByfilter.getDelFlag().equals("0")){
                //0 启用  1禁用  要在未删除的情况下   0未删除  1删除
                boolean updateState = hzhUserService.updateByState(Long.parseLong(id),status,updateDate);
                if (updateState){
                    return ResultVO.ok();
                }else {
                    return ResultVO.status(ResultEnum.INNER_EXCEPTION);
                }
            }else {
                return ResultVO.status(ResultEnum.VALIDATE_ERROR);
            }
        }else {
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }
    }

    //删除用户
    @PutMapping("/user/delUserById")
    public ResultVO delUserById(@RequestParam("id")String id,@RequestParam("delFlag")String delFlag){
        String updateDate = DateUtils.getCurrent(DateUtils.dateFullPattern);
        if ( !StringUtils.isEmpty(id) ){
            HzhUser findByfilter = hzhUserService.findByFitler(Long.parseLong(id));
            if (findByfilter != null ){
                // 0未删除  1删除
                boolean updateState = hzhUserService.delUserById(Long.parseLong(id),delFlag,updateDate);
                if (updateState){
                    return ResultVO.ok();
                }else {
                    return ResultVO.status(ResultEnum.INNER_EXCEPTION);
                }
            }else {
                return ResultVO.ok(ResultEnum.VALIDATE_ERROR);
            }
        }else {
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }
    }



}
