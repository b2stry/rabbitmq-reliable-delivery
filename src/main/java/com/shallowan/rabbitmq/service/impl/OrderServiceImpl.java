package com.shallowan.rabbitmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.shallowan.rabbitmq.constant.Constants;
import com.shallowan.rabbitmq.entity.BrokerMessageLog;
import com.shallowan.rabbitmq.entity.Order;
import com.shallowan.rabbitmq.mapper.BrokerMessageLogMapper;
import com.shallowan.rabbitmq.mapper.OrderMapper;
import com.shallowan.rabbitmq.producer.RabbitOrderSender;
import com.shallowan.rabbitmq.service.OrderService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author shallowan
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Override
    public void createOrder(Order order) throws Exception {
        // 插入业务数据
        orderMapper.insert(order);
        // 插入消息记录表数据
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        // 消息唯一ID
        brokerMessageLog.setMessageId(order.getMessageId());
        // 保存消息整体转为JSON格式存储入库
        brokerMessageLog.setMessage(JSON.toJSONString(order));
        // 设置消息状态为0 表示发送中
        brokerMessageLog.setStatus(Constants.ORDER_SENDING);
        Date nextRetry = DateUtils.addMinutes(new Date(), Constants.ORDER_TIMEOUT);
        brokerMessageLog.setNextRetry(nextRetry);
        brokerMessageLog.setTryCount(0);
        brokerMessageLogMapper.insert(brokerMessageLog);
        //发送消息
        rabbitOrderSender.sendOrder(order);
    }
}
