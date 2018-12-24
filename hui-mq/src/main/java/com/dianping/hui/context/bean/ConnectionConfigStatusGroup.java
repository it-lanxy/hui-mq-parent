package com.dianping.hui.context.bean;

import com.dianping.hui.bean.ClientType;
import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.bean.IClient;
import com.dianping.hui.context.util.ConnectionConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: lanxinyu@meituan.com  2018-11-22 4:58 PM
 * @Description: 配置集群共享，如果为空重新加载。
 */
@Slf4j
public class ConnectionConfigStatusGroup extends ConcurrentHashMap<String, ConnectionConfig> {

    private boolean inited = false;
    private static final String PREFIX = ":";
    private static final int APPKEY_INDEX = 0;
    private static final int RPC_URL_INDEX = 0;
    private static final int RPC_PORT_INDEX = 1;
    private static final int THRIFT_TIMEOUT_INDEX = 3;
    private static final int PIGEON_TIMEOUT_INDEX = 2;
    private static final int THRIFT_ARRAY_LENGTH = 4;
    private static final int PIGEON_ARRAY_LENGTH = 3;

    /**
     * 可上线的配置
     */

    private Map<String, ConnectionConfig> ONLINE_CONFIG = new ConcurrentHashMap();
    /**
     * 可下线的配置
     */
    private Map<String, ConnectionConfig> OFFLINE_CONFIG = new ConcurrentHashMap();


    public Map<String, ConnectionConfig> getOnlineConfig() {
        return ONLINE_CONFIG;
    }

    public Map<String, ConnectionConfig> getOfflineConfig() {
        return OFFLINE_CONFIG;
    }

    public Map<String, ConnectionConfig> getAllConfig() {
        return this;
    }

    /**
     * 1，所有配置转化并存储在当前类中
     * 2，可上线配置存储在 ONLINE_CONFIG 中
     * 3，可下线配置存储在 OFFLINE_CONFIG 中
     * @param entity
     */
    private void inject(ConnectionConfigEntity entity) {
        String key = Helper.generateKey(entity);
        ConnectionConfig allConfigs;
        if(super.contains(key))
            allConfigs = super.get(key);
        else {
            allConfigs = new ConnectionConfig();
            super.put(key, allConfigs);
        }
        ConnectionConfigHelper.config(key, entity, allConfigs);
        if(entity.getIsPublish()) {
            ConnectionConfig onlineConfigs;
            if(ONLINE_CONFIG.containsKey(key))
                onlineConfigs = ONLINE_CONFIG.get(key);
            else {
                onlineConfigs = new ConnectionConfig();
                ONLINE_CONFIG.put(key, onlineConfigs);
            }
            ConnectionConfigHelper.config(key, entity, onlineConfigs);
        } else {
            ConnectionConfig offlineConfigs;
            if(OFFLINE_CONFIG.containsKey(key))
                offlineConfigs = OFFLINE_CONFIG.get(key);
            else {
                offlineConfigs = new ConnectionConfig();
                OFFLINE_CONFIG.put(key, offlineConfigs);
            }
            ConnectionConfigHelper.config(key, entity, offlineConfigs);
        }
    }

    public synchronized void init(Collection<ConnectionConfigEntity> entityList) {
        if(!inited) {
            if(CollectionUtils.isEmpty(entityList))
                return;
            entityList.forEach(
                    entity -> inject(entity)
            );
            inited = Boolean.TRUE;
        } else {
            log.warn("ConnectionConfigStatusGroup.init inited!");
        }

    }

    public static class Helper {

        public static final String generateKey(ConnectionConfigEntity entity) {
            if(entity.getRpcTimeout() == null)
                entity.setRpcTimeout(IClient.DEFAULT_TIMEOUT);
            if(entity.getIsPublish() == null)
                entity.setIsPublish(Boolean.FALSE);
            switch (entity.getRpcType()) {
                case PIGEON:
                    return new StringBuilder(entity.getRpcUrl())
                            .append(PREFIX).append(ClientType.PIGEON)
                            .append(PREFIX).append(entity.getRpcTimeout()).toString();
                case THRIFT:
                    return new StringBuilder(entity.getRpcAppkey())
                            .append(PREFIX).append(entity.getRpcPort())
                            .append(PREFIX).append(ClientType.THRIFT)
                            .append(PREFIX).append(entity.getRpcTimeout()).toString();
                default:
                    throw new IllegalArgumentException("ConnectionConfigStatusGroup.generateKey error [known ClientType]");
            }
        }

        public static final String[] analyseKey(String key) {
            return key.split(PREFIX);
        }

        public static String analyseRpcAppkey(String[] array) {
            return ClientType.THRIFT.equals(analyseRpcType(array)) ?
                    array[APPKEY_INDEX] : StringUtils.EMPTY;
        }

        public static Integer analyseRpcPort(String[] array) {
            return ClientType.THRIFT.equals(analyseRpcType(array)) ?
                    Integer.parseInt(array[RPC_PORT_INDEX]) : null;
        }

        public static String analyseRpcUrl(String[] array) {
            return ClientType.PIGEON.equals(analyseRpcType(array)) ?
                    array[RPC_URL_INDEX] : StringUtils.EMPTY;
        }

        public static Integer analyseRpcTimeout(String[] array) {
            switch (analyseRpcType(array)) {
                case THRIFT:
                    return Integer.parseInt(array[THRIFT_TIMEOUT_INDEX]);
                case PIGEON:
                    return Integer.parseInt(array[PIGEON_TIMEOUT_INDEX]);
                default:
                    return IClient.DEFAULT_TIMEOUT;
            }
        }

        public static ClientType analyseRpcType(String[] array) {
            if(array.length == THRIFT_ARRAY_LENGTH) {
                return ClientType.THRIFT;
            }
            if(array.length == PIGEON_ARRAY_LENGTH) {
                return ClientType.PIGEON;
            }
            return ClientType.UNKNOWN;
        }
    }

}
