package com.shallowan.rabbitmq;

import com.shallowan.rabbitmq.entity.Order;
import com.shallowan.rabbitmq.producer.RabbitOrderSender;
import com.shallowan.rabbitmq.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private OrderService orderService;

    @Test
    public void testSender() throws Exception {
        Order order = new Order();
        order.setId("20181116000000001");
        order.setName("海鹚科技-郑大附三-门诊缴费");
        order.setMessageId(System.currentTimeMillis() + "#" + UUID.randomUUID().toString());
        rabbitOrderSender.sendOrder(order);
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId("20181116000000002");
        order.setName("海鹚科技-郑大附三-门诊缴费");
        order.setMessageId(System.currentTimeMillis() + "#" + UUID.randomUUID().toString());
        orderService.createOrder(order);
    }
}
