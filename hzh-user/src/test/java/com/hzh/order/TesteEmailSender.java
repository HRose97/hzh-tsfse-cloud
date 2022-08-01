package com.hzh.order;

import com.hzh.user.utils.EmailSender;

import javax.mail.MessagingException;

/**
 * 测试邮箱发送
 *
 * @author Hou Zhonghu
 * @since 2022/7/28 23:36
 */
public class TesteEmailSender {

    public static void main(String[] args) throws MessagingException {
       EmailSender.subject("测试主题发送")         //主题
               .from("体育赛事售票系统")            //发件人
               .text("这是发送的内容：ab235g")      //text 是文本  以及内容
               .to("1391210212@qq.com")          //收件人  可以多分
               .send();
        System.out.println("发送完成");

    }


}
