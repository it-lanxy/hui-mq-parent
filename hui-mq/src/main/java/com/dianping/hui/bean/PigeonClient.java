package com.dianping.hui.bean;

import com.dianping.hui.mq.pigeon.IConsumerGateway;
import com.dianping.hui.util.ConsumeStatusConverter;
import com.meituan.mafka.client.consumer.ConsumeStatus;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 5:15 PM
 * @Description:
 */
@AllArgsConstructor
@ToString
public class PigeonClient implements IClient {

    private IConsumerGateway client;

    @Override
    public boolean ping() {
        return client.ping();
    }

    @Override
    public ConsumeStatus consume(String bizAlias, String data) {
        return ConsumeStatusConverter.convert(client.consume(bizAlias, data));
    }
}
