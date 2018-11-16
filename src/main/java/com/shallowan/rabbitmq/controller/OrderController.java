package com.shallowan.rabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.shallowan.rabbitmq.entity.Order;
import com.shallowan.rabbitmq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Map<String, String> createOrder(Order order) {
        log.info(JSON.toJSONString(order));
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isAnyBlank(order.getId(), order.getName())) {
            map.put("code", "-1");
            map.put("msg", "orderId or orderName is not null");
            return map;
        }

        String messageId = System.currentTimeMillis() + "#" + UUID.randomUUID().toString();
        order.setMessageId(messageId);
        try {
            orderService.createOrder(order);
        } catch (Exception e) {
            log.error("exception:{}", e);
            map.put("code", "-2");
            map.put("msg", e.getMessage());
            return map;
        }
        map.put("code", "0");
        map.put("msg", "success");
        map.put("data", JSON.toJSONString(order));
        return map;
    }
}
