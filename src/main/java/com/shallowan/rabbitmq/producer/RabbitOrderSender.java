package com.shallowan.rabbitmq.producer;

import com.shallowan.rabbitmq.constant.Constants;
import com.shallowan.rabbitmq.entity.Order;
import com.shallowan.rabbitmq.mapper.BrokerMessageLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitOrderSender {
    // 自动注入RabbitTemplate模板类
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("correlationData" + correlationData);
            String messageId = correlationData.getId();
            if (ack) {
                log.info("投递成功");
                // 如果confirm返回成功，则进行更新
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS);
            } else {
                //失败则进行具体的后续操作：重试或者补偿等手段
                log.error("异常处理");
            }
        }
    };

    // 发送消息方法调用：构建自定义对象消息
    public void sendOrder(Order order) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange", "order.hc", order, correlationData);
    }
}
