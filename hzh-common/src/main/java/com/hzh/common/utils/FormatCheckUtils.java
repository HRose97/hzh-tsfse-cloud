package com.hzh.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式校验工具类
 *
 * @author Hou Zhonghu
 * @since 2022/7/29 10:56
 */
public class FormatCheckUtils {



    //数字
    private static final String REG_NUMBER = ".*\\d+.*";
    //小写字母
    private static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //大写字母
    private static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)
    private static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";



    /**
     * @return
     * @Author Hou zhonghu
     * @Description 邮箱校验方法
     * @Date 2022/7/29 10:56
     * @Param
     **/
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * @return
     * @Author Hou zhonghu
     * @Description 手机号校验方法
     * @Date 2022/7/29 10:57
     * @Param
     **/
    public static boolean isMobile(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */

        // "[1]"代表下一位为数字可以是几，
        // "[0-9]"代表可以为0-9中的一个，
        // "[5,7,9]"表示可以是5,7,9中的任意一位,
        // [^4]表示除4以外的任何一个,
        // \\d{9}"代表后面是可以是0～9的数字，有9位。

        String str = mobileNums;
        String pattern = "^(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57]|19[89]|166)[0-9]{8}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }


    /**
     * @description: 密码强度校验
     * 1) 密码控制只能输入字母、数字、特殊符号(~!@#$%^&*()_+[]{}|\;:'",./<>?)
     * 2) 长度 8-30 位，必须包括大小写字母、数字、特殊符号中的3种
     * 3) 密码不能包含用户名信息
     * @author: wenbsu
     * @time: 2022/8/02 14:04
     **/

    public static boolean checkPasswordRule(String password, String username) {

        //密码为空及长度大于8位小于30位判断
        if (password == null || password.length() < 8 || password.length() > 30) return false;

        int i = 0;

        if (password.matches(FormatCheckUtils.REG_NUMBER)) i++;
        if (password.matches(FormatCheckUtils.REG_LOWERCASE)) i++;
        if (password.matches(FormatCheckUtils.REG_UPPERCASE)) i++;
        if (password.matches(FormatCheckUtils.REG_SYMBOL)) i++;

        boolean contains = password.contains(username);

        if (i < 3 || contains) return false;

        return true;
    }

    //验证密码校验
/*    public static void main(String[] args) {
        boolean flag = checkPasswordRule("5q6W7e8R!", "wenbsu");
        System.out.println(flag);
    }*/


}

