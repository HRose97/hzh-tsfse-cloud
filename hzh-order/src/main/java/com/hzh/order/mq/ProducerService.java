package com.hzh.order.mq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息生产者
 *
 * @author Hou Zhonghu
 * @since 2022/6/29 12:14
 */
@Component("ProducerService")
public class ProducerService {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    //发送简单消息
    public void sentMsg(){
        for (int i=0; i < 10; i++){
            rocketMQTemplate.convertAndSend("hzh-demo1","简单消息  Hello rocketmq!"+i);
        }
    }

    //发送同步消息
    public void sendSyncMessage(){
        for (int i=0; i < 10; i++) {
            SendResult sendResult = rocketMQTemplate.syncSend("hzh-sftse-demo1", "同步消息！" + i);
            System.out.println(sendResult);
        }
    }

    //发送异步消息
    public void sendAsynMessage(){
        for (int i=0; i < 10; i++) {
        rocketMQTemplate.asyncSend("hzh-demo1", "发送异步消息！" + i, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("异步消息发送成功");
                }

                //也可进行重试
                @Override
                public void onException(Throwable throwable) {
                    System.out.println("异步消息发送失败");
                }
            });

        }
    }

    //发送单向消息   ------   一般用于日志
    public void sendOneWay(){
        for (int i=0; i < 10; i++) {
            rocketMQTemplate.sendOneWay("hzh-demo1", "单向消息！" + i);
        }

    }

    //发送同步消息
    public void syncSendOrderly(){
        for (int i=0; i < 10; i++) {
            rocketMQTemplate.syncSendOrderly("hzh-demo2-syncSeq", "165464 创建" ,"165464");
            rocketMQTemplate.syncSendOrderly("hzh-demo2-syncSeq", "165464 支付" ,"165464");
            rocketMQTemplate.syncSendOrderly("hzh-demo2-syncSeq", "165464 完成" ,"165464");

            rocketMQTemplate.syncSendOrderly("hzh-demo2-syncSeq", "789799 创建" ,"789799");
            rocketMQTemplate.syncSendOrderly("hzh-demo2-syncSeq", "789799 支付" ,"789799");
            rocketMQTemplate.syncSendOrderly("hzh-demo2-syncSeq", "789799 完成" ,"789799");
        }
    }

    //发送延迟消息、
/*
    public void sendDalyMessage(){
        rocketMQTemplate.asyncSend("hzh-demo1", MessageBuilder.withPayload("rocketmq发送延迟1s消息").build(),3000,1);
        rocketMQTemplate.asyncSend("hzh-demo1", MessageBuilder.withPayload("rocketmq发送延迟5s消息").build(), 3000);
        rocketMQTemplate.asyncSend("hzh-demo1", MessageBuilder.withPayload("rocketmq发送延迟10s消息").build(), 3000);
    }
*/


    //发送事务消息
    public void sendTransactionMessage(){
        //构造信息
        Message message = MessageBuilder.withPayload("rocketmq事务消息-01").build();
        rocketMQTemplate.sendMessageInTransaction("hzh-demo4-tarnsation", message,null);
    }


    //本地事务和发送事务消息是一起的
    @RocketMQTransactionListener
    class TransactionListenerImpl implements RocketMQLocalTransactionListener {

        //executeLocalTransaction，当我们处理完业务后，可以根据业务处理情况，返回事务执行状态，有bollback,commit or unknown三种，分别是
        //回滚事务，提交事务和未知，根据事务消息执行流程，如果是rockball,则直接丢弃消息；如果返回commit，则消息消息；如果是unknown的情况，则继续、
        //等待，checkLocalTransaction方法 最多重试15次后，默认丢弃此消息
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
            System.out.println("executeLocalTransaction");
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        //该方法是当MQ server未得到MQ发送方应答，或者超时的情况，或者应答是unknown的情况，调用方法进行检查确认，返回值和上面的方法一样
        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
            System.out.println("checkLocalTransaction");
            return RocketMQLocalTransactionState.COMMIT;
        }
    }


    //发送带tag消息，测试根据tag过滤消息
    public void sendMessageWithTag(){
        //构造信息1
        Message message1 = MessageBuilder.withPayload("rocketmq过滤消息测试-01").build();
        Message message2 = MessageBuilder.withPayload("rocketmq过滤消息测试-02").build();
        Message message3 = MessageBuilder.withPayload("rocketmq过滤消息测试-03").build();

        rocketMQTemplate.convertAndSend("hzh-demo5-filter" + ":" + "TAG1" , message1);
        rocketMQTemplate.convertAndSend("hzh-demo5-filter" + ":" + "TAG2" , message2);
        rocketMQTemplate.convertAndSend("hzh-demo5-filter" + ":" + "TAG3" , message3);
    }



    //发送SQL表达式头信息消息，测试SQL表达式过滤消息
    public void sendMessageWithSQL() {
        //构造信息1
        Message message1 = MessageBuilder.withPayload("rocketmq过滤SQL消息测试-01").build();
        Map<String,Object> headers = new HashMap<>();
        headers.put("type","pay");
        headers.put("a",10);
        rocketMQTemplate.convertAndSend("hzh-demo6-filterSQL",message1,headers);

        //构造信息1
        Message message2 = MessageBuilder.withPayload("rocketmq过滤SQL消息测试-02").build();
        Map<String,Object> headers2 = new HashMap<>();
        headers.put("type","store");
        headers.put("a",4);
        rocketMQTemplate.convertAndSend("hzh-demo6-filterSQL",message2,headers2);

        //构造信息1
        Message message3 = MessageBuilder.withPayload("rocketmq过滤SQL消息测试-03").build();
        Map<String,Object> headers3 = new HashMap<>();
        headers.put("type","user");
        headers.put("a",7);
        rocketMQTemplate.convertAndSend("hzh-demo6-filterSQL",message2,headers3);



    }


















}
