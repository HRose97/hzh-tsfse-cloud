package com.hzh.test.mybatis.controller;

import com.hzh.test.mybatis.pojo.MoseSystemHost;
import com.hzh.test.mybatis.pojo.vo.Messager;
import com.hzh.test.mybatis.service.TabelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 利用mybatis对表的操作
 *
 * @author Hou Zhonghu
 * @since 2022/9/5 19:06
 */
@RestController
@RequestMapping("/table")
public class TabelController {


    @Resource
    public TabelService tabelService;

    @GetMapping("/hzh/test")
    public String test(){
        return "nihao";
    }


    @PostMapping("/hzh/CreateTable")
    public Messager CreateTable(@RequestBody MoseSystemHost moseSystemHost){
        Messager messager = new Messager();
        boolean flag = false;
        try {
            messager = tabelService.selHostInfoByHostCode(moseSystemHost);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("建表异常");
        }finally {
            if (flag){
                return messager;
            }else {
                return messager;
            }
        }
    }

    @PostMapping("/hzh/delTable")
    public Messager delTable(@RequestBody MoseSystemHost moseSystemHost){
        Messager messager = tabelService.delHostCode(moseSystemHost.getStrSystemCode(),moseSystemHost.getStrHostCode());
        return messager;
    }


}
