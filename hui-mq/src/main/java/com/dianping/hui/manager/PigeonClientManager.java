package com.dianping.hui.manager;

import com.dianping.hui.bean.IClient;
import com.dianping.hui.bean.PigeonClient;
import com.dianping.hui.context.bean.RpcConnectionConfig;
import com.dianping.hui.context.factory.IClientManager;
import com.dianping.hui.mq.pigeon.IConsumerGateway;
import com.dianping.pigeon.remoting.ServiceFactory;
import com.dianping.pigeon.remoting.invoker.config.InvokerConfig;
import org.springframework.stereotype.Component;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 5:35 PM
 * @Description:
 */
@Component
public class PigeonClientManager implements IClientManager {

    @Override
    public IClient create(RpcConnectionConfig entity) {
        InvokerConfig<IConsumerGateway> config = new InvokerConfig<>(IConsumerGateway.class);
        config.setSerialize(InvokerConfig.SERIALIZE_HESSIAN);
        config.setCallType(InvokerConfig.CALL_SYNC);
        config.setUrl(entity.getRpcUrl());
        config.setTimeout(entity.getRpcTimeout());
        return new PigeonClient(ServiceFactory.getService(config));
    }
}
