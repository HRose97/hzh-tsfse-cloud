package com.hzh.user.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.HzhUser;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.respone.R;
import com.hzh.common.utils.DateUtils;
import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户列表
 *
 * 该控制类中的请求只允许admin用户访问
 *
 * @author Hou Zhonghu
 * @since 2022/8/13 18:56
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class HzhUserAdminController {

    @Resource
    private HzhUserService hzhUserService;

    //重置密码  管理员重置
    @GetMapping("/user/reSetPasswordByAdmin")
    public R reSetPasswordByAdmin(@RequestParam(value = "id")String id,
                                         @RequestBody ReSetPasswordVo reSetPasswordVo) throws Exception {
        return hzhUserService.reSetPasswordByAdmin(id,reSetPasswordVo);
    }

    //分页查询
    @GetMapping("/user/getAllUserByPage")
    public R getAllUserByPage(@RequestParam("current")String current,@RequestParam("size")String size){
        if ( !StringUtils.isEmpty(current) && !StringUtils.isEmpty(current) ){
            Page<HzhUser> page = new Page<>(Long.parseLong(current), Long.parseLong(size));
            IPage<HzhUser> hzhUserIPage = hzhUserService.findAllByPage(page);
            return R.SUCCESS("查询成功",hzhUserIPage);
        }else {
            return R.FAILED("查询失败");
        }
    }

    //TODO 待完善  查询语句有问题
    @GetMapping("/user/getUserInfoByFilter")
    public R getUserInfoByFilter(@RequestParam(value = "phone")String phone,
                                 @RequestParam(value = "email")String email,
                                 @RequestParam(value = "userName")String userName,
                                 @RequestParam(value = "level")String level,
                                 @RequestParam(value = "userType")String userType,
                                 @RequestParam(value = "status")String status
                                 ){
        // 根据用户名
        // 根据邮箱
        // 根据手机号
        //根据创建时间
        // 根据用户类型
        HzhUser hzhUser = new HzhUser();
        hzhUser.setUserName(userName);
        hzhUser.setEmail(email);
        hzhUser.setUserType(userType);
        hzhUser.setPhonenumber(phone);
        hzhUser.setStatus(status);
        hzhUser.setLevel(level);
        Page<HzhUser> page = new Page<>(0, 10);
        IPage<HzhUser> hzhUserIPage = hzhUserService.selectListByFilter(page,hzhUser);
        System.out.println(hzhUserIPage);
        return R.SUCCESS("查询成功",hzhUserIPage);
    }

    //禁用用户
    @PutMapping("/user/disableUserById")
    public ResultVO disableUserById(@RequestParam("id")String id,@RequestParam("status")String status){
        String updateDate = DateUtils.getCurrent(DateUtils.dateFullPattern);
        if ( !StringUtils.isEmpty(id) ){
            HzhUser findByfilter = hzhUserService.findByUserId(Long.parseLong(id));
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
            HzhUser findByfilter = hzhUserService.findByUserId(Long.parseLong(id));
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

    //管理员初始化
    @PutMapping("/user/initAdminAccount")
    public R initAdminAccount(@RequestBody HzhUser hzhUser){
        //TODO P66觉得没有必要初始化  有必要时在做
        return null;
    }



}
