package com.shallowan.rabbitmq.constant;

/**
 * @author shallowan
 */
public final class Constants {
    //发送中
    public static final String ORDER_SENDING = "0";
    //成功
    public static final String ORDER_SEND_SUCCESS = "1";
    //失败
    public static final String ORDER_SEND_FAILURE = "2";
    //超时单位：min
    public static final int ORDER_TIMEOUT = 5;
    //最大重试次数
    public static final int MAX_RETRY_COUNT = 3;
}
