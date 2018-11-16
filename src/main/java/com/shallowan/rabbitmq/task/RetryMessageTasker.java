package com.shallowan.rabbitmq.task;

import com.alibaba.fastjson.JSON;
import com.shallowan.rabbitmq.constant.Constants;
import com.shallowan.rabbitmq.entity.BrokerMessageLog;
import com.shallowan.rabbitmq.entity.Order;
import com.shallowan.rabbitmq.mapper.BrokerMessageLogMapper;
import com.shallowan.rabbitmq.producer.RabbitOrderSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author shallowan
 */
@Component
@Slf4j
@EnableScheduling
public class RetryMessageTasker {
    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend() {
        //拉去status=0并且超时next_retry时间的message
        List<BrokerMessageLog> brokerMessageLogs = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        brokerMessageLogs.forEach(messageLog -> {
            if (messageLog.getTryCount() >= Constants.MAX_RETRY_COUNT) {
                //消息投递失败
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE);
            } else {
                // 重发
                Date nextRetry = DateUtils.addMinutes(new Date(), Constants.ORDER_TIMEOUT);
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(), nextRetry);
                Order reSendOrder = JSON.parseObject(messageLog.getMessage(), Order.class);
                try {
                    rabbitOrderSender.sendOrder(reSendOrder);
                } catch (Exception e) {
                    log.error("异常处理==>{}", e);
                }
            }
        });
    }
}
