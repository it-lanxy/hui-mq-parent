package com.dianping.hui.context.support;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.bean.IClient;
import com.dianping.hui.context.IConfigContext;
import com.dianping.hui.context.bean.ActionType;
import com.dianping.hui.context.bean.ConnectionConfig;
import com.dianping.hui.context.bean.ConnectionConfigStatusGroup;
import com.dianping.hui.context.bean.ConnectionStatusMark;
import com.dianping.hui.context.factory.ConsumerGatewayFactory;
import com.dianping.hui.manager.MafkaConsumerManager;
import com.dianping.hui.util.IConverter;
import com.google.common.collect.Lists;
import com.meituan.mafka.client.consumer.IConsumerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: lanxinyu@meituan.com  2018-11-27 7:28 PM
 * @Description:
 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
 */
@Slf4j
abstract class AbstractConfigContext implements IDisasterTolerance, IConfigContext,
        InitializingBean, BeanPostProcessor {

    @Autowired
    MafkaConsumerManager mafkaConsumerManager;
    @Autowired
    ConsumerGatewayFactory factory;

    @Value("${app.name}")
    String appKey;

    List<ConnectionConfigEntity> originalBackupConfig = Lists.newArrayList();
    List<ConnectionConfigEntity> originalCurrentConfig = Lists.newArrayList();
    ConnectionConfigStatusGroup backupConfig;
    ConnectionConfigStatusGroup currentConfig;

    @Override
    public synchronized void refresh() {
        refreshConnectionConfig();
        tryOffline();
        tryOnline();
    }

    private void tryOnline() {
        if(CollectionUtils.isEmpty(originalCurrentConfig))
            return;
        Collection<ConnectionConfigEntity> onlineList = CollectionUtils.subtract(originalCurrentConfig, originalBackupConfig);
        ConnectionConfigStatusGroup increment = new ConnectionConfigStatusGroup();
        increment.init(onlineList);
        Map<String, ConnectionConfig> incrementMap = increment.getOnlineConfig();
        incrementMap.keySet().forEach(
                key -> {
                    try {
                        online(key, incrementMap.get(key));
                    } catch (Exception e) {
                        log.error("加载配置失败", e);
                    }
                }
        );
    }

    private void tryOffline() {
        if(CollectionUtils.isEmpty(originalBackupConfig))
            return;
        Collection<ConnectionConfigEntity> offlineList = CollectionUtils.subtract(originalBackupConfig, originalCurrentConfig);
        offlineList.forEach(
                offline -> offline(offline)
        );
    }

    private void online(String key, ConnectionConfig config) {
        IClient client;
        ConnectionStatusMark csm = new ConnectionStatusMark();
        csm.setIds(config.getIds());
        csm.setTimestamp(System.currentTimeMillis());
        try {
            if(IClient.Cache.CLIENT_ALIVE.containsKey(key))
                client = IClient.Cache.CLIENT_ALIVE.get(key);
            else {
                client = factory.pickIClientManager(config.getRpcConnectionConfig().getRpcType()).create(config.getRpcConnectionConfig());
                IClient.Cache.CLIENT_ALIVE.put(key, client);
            }
            Assert.isTrue(client.ping(), "初始化接入方失败");
            csm.setAction(ActionType.CONNECTION_SUCCESS);
        } catch (Exception e) {
            csm.setAction(ActionType.CONNECTION_FAIL);
            throw e;
        } finally {
            getConnectionOperationFlowManager().append(csm);
        }

        Set<IConsumerProcessor> consumer = mafkaConsumerManager.onLine(config, client);
        Assert.notEmpty(consumer, "初始化消费者失败");
    }

    private boolean offline(ConnectionConfigEntity entity) {
        Long id = entity.getId();
        IConsumerProcessor consumer = MafkaConsumerManager.CONSUMER_ALIVE.get(id);
        boolean result = Boolean.TRUE;
        if(consumer != null) {
            ConnectionStatusMark csm = new ConnectionStatusMark();
            csm.setId(id);
            csm.setTimestamp(System.currentTimeMillis());
            try {
            } finally {
                try {
                    consumer.close();
                    MafkaConsumerManager.CONSUMER_ALIVE.remove(id);
                    csm.setAction(ActionType.OFF_LINE_SUCCESS);
                } catch (Exception e) {
                    result = Boolean.FALSE;
                    log.error("AbstractConfigContext.offLine error :", e);
                    csm.setAction(ActionType.OFF_LINE_FAIL);
                }
            }
            getConnectionOperationFlowManager().append(csm);
        }
        return result;

    }

    private void refreshConnectionConfig() {
        //备份
        originalBackupConfig = IConverter.INSTANCE.connectionConfigEntityListBackup(originalCurrentConfig);
        backupConfig = IConverter.INSTANCE.connectionConfigStatusGroupBackUp(currentConfig);

        //重载
        originalCurrentConfig = getAllConnectionConfig();
        currentConfig = new ConnectionConfigStatusGroup();

        //缓存全量数据
        currentConfig.init(originalCurrentConfig);
    }

    private List<ConnectionConfigEntity> getAllConnectionConfig() {
        try {
            return getConfigManager().findAll();
        } catch (Exception e1) {
            log.error("主数据源加载数据失败，从数据源中加载:", e1);
            try {
                return getBackupConfigManager().findAll();
            } catch (Exception e2) {
                log.error("从数据源中加载异常:", e2);
                return Lists.newArrayList();
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        refresh();
    }

}
