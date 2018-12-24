package com.dianping.hui.bean;

import com.dianping.hui.mq.thrift.IConsumerGateway;
import com.dianping.hui.util.ConsumeStatusConverter;
import com.meituan.mafka.client.consumer.ConsumeStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 5:15 PM
 * @Description:
 */
@Slf4j
@AllArgsConstructor
public class ThriftClient implements IClient {

    private IConsumerGateway.Iface client;

    @Override
    public boolean ping() {
        try {
            return client.ping();
        } catch (Exception e) {
            log.error("ThriftClient.ping error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public ConsumeStatus consume(String bizAlias, String data) {
        try {
            return ConsumeStatusConverter.convert(client.consume(bizAlias, data));
        } catch (Exception e) {
            log.error("ThriftClient.consume error:", e);
            return ConsumeStatus.RECONSUME_LATER;
        }
    }
}
