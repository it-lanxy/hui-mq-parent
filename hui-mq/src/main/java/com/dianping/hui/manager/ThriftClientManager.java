package com.dianping.hui.manager;

import com.dianping.hui.bean.IClient;
import com.dianping.hui.context.bean.RpcConnectionConfig;
import com.dianping.hui.bean.ThriftClient;
import com.dianping.hui.context.factory.IClientManager;
import com.dianping.hui.mq.thrift.IConsumerGateway;
import com.meituan.service.mobile.mtthrift.proxy.ThriftClientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 5:35 PM
 * @Description:
 */
@Component
@Slf4j
public class ThriftClientManager implements IClientManager {

    @Value("${app.name}")
    private String appKey;

    @Override
    public IClient create(RpcConnectionConfig entity) {
        try {
            ThriftClientProxy proxy = new ThriftClientProxy();
            proxy.setAppKey(appKey);
            proxy.setRemoteAppkey(entity.getRpcAppkey());
            proxy.setServiceInterface(IConsumerGateway.class);
            proxy.setRemoteServerPort(entity.getRpcPort());
            proxy.setTimeout(entity.getRpcTimeout());
            proxy.afterPropertiesSet();
            return new ThriftClient((IConsumerGateway.Iface) proxy.getObject());
        } catch (Exception e) {
            log.error("ThriftClientManager.create error :" ,e);
            return null;
        }
    }
}
