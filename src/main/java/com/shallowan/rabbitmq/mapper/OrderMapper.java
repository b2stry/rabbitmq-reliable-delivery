package com.shallowan.rabbitmq.mapper;

import com.shallowan.rabbitmq.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface OrderMapper {
    /**
     * 插入订单
     *
     * @param order
     * @return
     */
    int insert(Order order);
}
