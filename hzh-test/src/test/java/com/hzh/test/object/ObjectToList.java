package com.hzh.test.object;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Object转List
 *
 * @author Hou Zhonghu
 * @since 2022/9/5 15:12
 */
@SpringBootTest
public class ObjectToList {

    @Resource
    private BeanUtils beanutils;

    @Test
    public void ObjectToList(){

        Object obj = "[\n" +
                "\t{\n" +
                "\t\t\"借据号\"：\"51030110004144056\"，\n" +
                "\t\t\"当日发生额\"：0，\n" +
                "\t\t\"名称\"：\"对外标准正常本金\"，\n" +
                "\t\t\"当前余额\"：\"EXTZCBJ\"，\n" +
                "\t\t\"上日余额\"：0\n" +
                "\t}，\n" +
                "\t{\n" +
                "\t\t\"借据号\"：\"51030110004155987\"，\n" +
                "\t\t\"当日发生额\"：600，\n" +
                "\t\t\"名称\"：\"对外标准正常本金\"，\n" +
                "\t\t\"当前余额\"：\"EXTZCBJ\"，\n" +
                "\t\t\"上日余额\"：0\n" +
                "\t}，\n" +
                "\t{\n" +
                "\t\t\"借据号\"：\"51030110006987456\"，\n" +
                "\t\t\"当日发生额\"：1000，\n" +
                "\t\t\"名称\"：\"对外标准正常本金\"，\n" +
                "\t\t\"当前余额\"：\"EXTZCBJ\"，\n" +
                "\t\t\"上日余额\"：0\n" +
                "\t}，\n" +
                "\t{\n" +
                "\t\t\"借据号\"：\"51031224564144056\"，\n" +
                "\t\t\"当日发生额\"：8000，\n" +
                "\t\t\"名称\"：\"对外标准正常本金\"，\n" +
                "\t\t\"当前余额\"：\"EXTZCBJ\"，\n" +
                "\t\t\"上日余额\"：0\n" +
                "\t}]";

        Map<String,Object> map = JSON.parseObject(obj.toString().replace("\\",""),Map.class);
        System.out.println(map.get(0));

    }

}
