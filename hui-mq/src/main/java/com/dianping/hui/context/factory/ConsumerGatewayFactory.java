package com.dianping.hui.context.factory;

import com.dianping.hui.bean.ClientType;
import com.dianping.hui.bean.SourceType;
import com.dianping.hui.manager.LionConfigManager;
import com.dianping.hui.manager.PigeonClientManager;
import com.dianping.hui.manager.RAMOperationFlowManager;
import com.dianping.hui.manager.ThriftClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 2:31 PM
 * @Description:
 */
@Component
public class ConsumerGatewayFactory {

    @Autowired
    private LionConfigManager lionConfigManager;
    @Autowired
    private PigeonClientManager pigeonClientManager;
    @Autowired
    private ThriftClientManager thriftClientManager;
    @Autowired
    private RAMOperationFlowManager ramOperationFlowManager;

    public IConfigManager pickIConfigManager(SourceType type) {
        switch (type) {
            case LION: return lionConfigManager;
            default:
                throw new IllegalArgumentException();
        }
    }

    public IClientManager pickIClientManager(ClientType type) {
        switch (type) {
            case PIGEON: return pigeonClientManager;
            case THRIFT: return thriftClientManager;
            default:
                throw new IllegalArgumentException();
        }
    }

    public IConnectionOperationFlowManager pickIConnectionOperationFlowManager(SourceType type) {
        switch (type) {
            case RAM: return ramOperationFlowManager;
            default:
                throw new IllegalArgumentException();
        }
    }
}
