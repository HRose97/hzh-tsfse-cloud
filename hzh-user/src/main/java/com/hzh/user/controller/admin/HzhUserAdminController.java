package com.hzh.user.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.HzhUser;
import com.alibaba.excel.ExcelWriter;
import com.hzh.common.pojo.vo.ReSetPasswordVo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.respone.R;
import com.hzh.common.utils.DateUtils;
import com.hzh.user.service.HzhUserService;
import com.hzh.user.utils.ExcelUtils;
import com.hzh.user.utils.POIUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户列表
 *
 * 该控制类中的请求只允许admin用户访问
 *
 * @author Hou Zhonghu
 * @since 2022/8/13 18:56
 */
@Slf4j
@CrossOrigin
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

    //系统管理员单个新增用户
    @PostMapping("/user/addUserInfo")
    public R addUserInfo(@RequestBody HzhUser hzhUser){
        System.out.println("HHH ===> " + hzhUser);
        int i = hzhUserService.addUserByAdmin(hzhUser);
        if (i > 0){
            return R.SUCCESS("新增成功");
        }else {
            return R.FAILED("新增失败");
        }
    }

    /**
     * 导入 Excel 数据
     * 网上的
     * @param file 你要导入的 Excel 文件
     * @return  https://blog.csdn.net/qq_43647359/article/details/105296587
     * @throws IOException
     */
    @PostMapping("/user/import")
    public R importData(@RequestBody MultipartFile file) throws Exception {
//        System.out.println("file ===> " + file);
//        // 1.自定义一个工具类拿到要解析的文件并解析成要存储的数据
//        List<HzhUser> list = POIUtils.excel2Employee(file);
//        // 2.遍历输出你解析的数据格式是否正确
//        for (HzhUser employee : list) {
//            System.out.println(employee.toString());
//        }
//        // 3.进行数据库添加操作
//        if (hzhUserService.insert(list) == 1) {
//            return R.SUCCESS("上传成功！");
//        }
//        return R.FAILED("上传失败！");
        //===========================================
//        boolean upload = hzhUserService.uploadExcel(file);
//        if (upload){
//            return R.SUCCESS("导入成功");
//        }else {
//            return R.FAILED("导入失败");
//        }
        boolean b = hzhUserService.uploadExcel(file);
        if (b){
            return R.SUCCESS("导入成功");
        }
        return R.FAILED("导入失败");
    }


    //导出文件到本地
    @GetMapping("/user/ouPuttEcexl")
    public R ouPuttEcexl(){
        String path = "E:\\tsfse\\user\\user.xlsx";
        List<HzhUser> all = hzhUserService.getAll();
        ExcelWriter excelWriter = EasyExcel.write(path, HzhUser.class).build();
        //sheet名称
        WriteSheet build1 = EasyExcel.writerSheet("test").build();
        excelWriter.write(all,build1);
        excelWriter.finish();
        System.out.println("导出成功 ：" + path);
        return R.SUCCESS("导出成功");
    }



}
