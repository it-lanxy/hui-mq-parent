package com.dianping.hui.context.util;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.context.bean.ConnectionConfig;
import com.dianping.hui.context.bean.MafkaConnectionConfig;
import com.dianping.hui.context.bean.RpcConnectionConfig;
import com.dianping.hui.util.IConverter;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: lanxinyu@meituan.com  2018-11-26 7:03 PM
 * @Description:
 */
public class ConnectionConfigHelper {
    public static void config(String key, ConnectionConfigEntity entity, ConnectionConfig configs) {
        if(! contains(configs, entity.getId())) {
            configs.setRpcConnectionConfig(new RpcConnectionConfig().setKey(key));
            configs.setIds(addIds(configs, entity.getId()));
            configs.setMafkaConnectionConfigSet(addMafkaConfigs(configs, entity));
        }
    }

    private static Set<MafkaConnectionConfig> addMafkaConfigs(ConnectionConfig configs, ConnectionConfigEntity entity) {
        Set<MafkaConnectionConfig> mafkaConfigSet = configs.getMafkaConnectionConfigSet();
        MafkaConnectionConfig mafkaConfig =
                IConverter.INSTANCE.connectionConfigEntity2UniversalConnectionConfig(entity);
        if(CollectionUtils.isEmpty(mafkaConfigSet))
            mafkaConfigSet = new HashSet<>();
        mafkaConfigSet.add(mafkaConfig);
        return mafkaConfigSet;
    }

    private static Set<Long> addIds(ConnectionConfig configs, Long id) {
        Set<Long> idSet = configs.getIds();
        if(CollectionUtils.isEmpty(idSet))
            idSet = new HashSet<>();
        idSet.add(id);
        return idSet;
    }

    public static boolean contains(ConnectionConfig configs, Long id) {
        Set<Long> ids = configs.getIds();
        if(CollectionUtils.isEmpty(ids) || ! ids.contains(id))
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }


}
