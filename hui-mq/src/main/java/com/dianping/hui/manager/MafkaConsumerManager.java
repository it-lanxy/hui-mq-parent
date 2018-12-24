package com.dianping.hui.manager;

import com.dianping.hui.bean.IClient;
import com.dianping.hui.context.IConfigContext;
import com.dianping.hui.context.bean.ActionType;
import com.dianping.hui.context.bean.ConnectionConfig;
import com.dianping.hui.context.bean.ConnectionStatusMark;
import com.dianping.hui.context.bean.MafkaConnectionConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.meituan.mafka.client.MafkaClient;
import com.meituan.mafka.client.consumer.ConsumerConstants;
import com.meituan.mafka.client.consumer.IConsumerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 5:51 PM
 * @Description:
 */
@Component
@Slf4j
public class MafkaConsumerManager {

    public static Map<Long, IConsumerProcessor> CONSUMER_ALIVE = Maps.newConcurrentMap();

    @Autowired
    private IConfigContext configContext;
    /**
     * mafka 上线
     * @param config
     * @param client
     * @return
     */
    public Set<IConsumerProcessor> onLine(ConnectionConfig config, final IClient client) {
        Set<IConsumerProcessor> set = Sets.newHashSet();
        config.getMafkaConnectionConfigSet()
                .forEach(
                    mafkaConnectionConfig -> {
                        if(mafkaConnectionConfig.getIsPublish()) {
                            IConsumerProcessor consumer = createSingle(mafkaConnectionConfig, client);
                            if(consumer != null) {
                                set.add(consumer);
                                CONSUMER_ALIVE.put(mafkaConnectionConfig.getId(), consumer);
                            }
                        }
                    }
                );
        return set;
    }

    private IConsumerProcessor createSingle(MafkaConnectionConfig config, final IClient client) {
        Properties properties = new Properties();
        ConnectionStatusMark csm = new ConnectionStatusMark();
        csm.setId(config.getId());
        csm.setTimestamp(System.currentTimeMillis());
        properties.setProperty(ConsumerConstants.MafkaBGNamespace, config.getMfkBgNameSpace());
        properties.setProperty(ConsumerConstants.MafkaClientAppkey, config.getMfkClientAppkey());
        properties.setProperty(ConsumerConstants.SubscribeGroup, config.getMfkSubscribeGroup());
        IConsumerProcessor consumer = null;
        try {
            consumer = MafkaClient.buildConsumerFactory(properties, config.getMfkTopic());
        } catch (Exception e) {
            log.error("MafkaClientManager.createSingle error :" ,e);
            csm.setAction(ActionType.ON_LINE_FAIL);
            configContext.getConnectionOperationFlowManager().append(csm);
            return consumer;
        }
        consumer.recvMessageWithParallel(String.class, (message, context) ->
                client.consume(config.getBizAlias(), message.getBody().toString())
        );
        csm.setAction(ActionType.ON_LINE_SUCCESS);
        configContext.getConnectionOperationFlowManager().append(csm);
        return consumer;
    }


}
