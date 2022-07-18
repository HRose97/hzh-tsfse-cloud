package com.hzh.order.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * mq测试同步消息发送
 *
 * @author Hou Zhonghu
 * @since 2022/7/17 17:40
 */
public class SyncProducer {

    public static void main(String[] args) throws UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException, MQClientException {
        //实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer();

        //设置NameServer的地址
        producer.setNamesrvAddr("192.168.65.131:9876");
        //producer.setSendLatencyFaultEnable(true);

        //启动Producer实例
        for (int i=0; i < 10; i++){
            Message msg = new Message("TopicTest",   //Topic
                    "TagA" , //TAG
                    ("hello mq" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            //发送消息到一个Broker
            SendResult sendResult = producer.send(msg);
            System.out.println("mq:"+ sendResult);
        }
        //如果不在发送消息，关闭Producer实例
        producer.shutdown();
    }

}
