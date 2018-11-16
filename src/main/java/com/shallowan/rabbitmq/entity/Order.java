package com.shallowan.rabbitmq.entity;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shallowan
 */
@Data
public class Order implements Serializable {

    private String id;

    private String name;

    private String messageId;

}
