package com.shallowan.rabbitmq.mapper;

import com.shallowan.rabbitmq.entity.BrokerMessageLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Mapper
public interface BrokerMessageLogMapper {

    /**
     * 插入消息记录
     *
     * @param brokerMessageLog
     * @return
     */
    int insert(BrokerMessageLog brokerMessageLog);

    /**
     * 查询消息状态为0（发送中）且已经超时的消息集合
     *
     * @return
     */
    List<BrokerMessageLog> query4StatusAndTimeoutMessage();

    /**
     * 重新发送统计count发送次数 +1
     *
     * @param messageId
     * @param nextRetry
     */
    int update4ReSend(@Param("messageId") String messageId, @Param("nextRetry") Date nextRetry);

    /**
     * 更新最终消息发送结果  成功or失败
     *
     * @param messageId
     * @param status
     */
    int changeBrokerMessageLogStatus(@Param("messageId") String messageId, @Param("status") String status);
}
