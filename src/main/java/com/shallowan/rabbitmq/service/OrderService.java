package com.shallowan.rabbitmq.service;

import com.shallowan.rabbitmq.entity.Order;

/**
 * @author shallowan
 */
public interface OrderService {
    void createOrder(Order order) throws Exception;
}
