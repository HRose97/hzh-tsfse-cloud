package com.hzh.user.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式校验工具类
 *
 * @author Hou Zhonghu
 * @since 2022/7/29 10:56
 */
public class FormatCheckUtils {

     /**
      * @Author Hou zhonghu
      * @Description  邮箱校验方法
      * @Date 2022/7/29 10:56
      * @Param
      * @return
      **/
     public static boolean isEmail(String email){
         if (null==email || "".equals(email)) return false;
         //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
         Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
         Matcher m = p.matcher(email);
         return m.matches();
     }

     /**
      * @Author Hou zhonghu
      * @Description  手机号校验方法
      * @Date 2022/7/29 10:57
      * @Param 
      * @return 
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

    
}
