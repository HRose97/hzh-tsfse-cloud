package com.hzh.event.mq;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 *
 * @author Hou Zhonghu
 * @since 2022/6/29 13:08
 *
 * MessageModel 默认是并发的
 * //MessageModel.CLUSTERING是默认集群负载均衡模式      MessageModel.BROADCASTING是广播模式
 *
 * messageModel = MessageModel.CLUSTERING 负载均衡模式，即多个生产者只能由一个消费者消费，即生产 1-10条数据， 如果开启俩个消费者，则2者任意消费其中的消息，不重复
 *
 * MessageModel.BROADCASTING是广播模式   即每个消费者都消费一次
 *
 *
 *
 * consumeMode = ConsumeMode.ORDERLY 单线程      可改为上述的内容
 *
 *
 */
@Component
//多线程
//@RocketMQMessageListener(topic = "hzh-demo1", consumerGroup = "${rocketmq.consumer.group}", messageModel = MessageModel.BROADCASTING)
@RocketMQMessageListener(topic = "hzh-sftse-demo1", consumerGroup = "${rocketmq.consumer.group}", messageModel = MessageModel.BROADCASTING)
//单线程
//@RocketMQMessageListener(topic = "hzh-demo2-syncSeq", consumerGroup = "${rocketmq.consumer.group}",consumeMode = ConsumeMode.ORDERLY)
//事务消息
//@RocketMQMessageListener(topic = "hzh-demo4-tarnsation", consumerGroup = "${rocketmq.consumer.group}")
//过滤消息
//@RocketMQMessageListener(topic = "hzh-demo5-filter", consumerGroup = "${rocketmq.consumer.group}",selectorExpression = "TAG1 || TAG2",selectorType = SelectorType.TAG)
//发送SQL表达式头信息消息，测试SQL表达式过滤消息
//@RocketMQMessageListener(topic = "hzh-demo6-filterSQL", consumerGroup = "${rocketmq.consumer.group}",selectorExpression = "type = 'user' or a < 11",selectorType = SelectorType.SQL92)
public class ConsumerService implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println("消费者1  8083   收到消息内容："+ s );
    }
}
