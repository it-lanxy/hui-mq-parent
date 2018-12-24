package com.dianping.hui.bean;

import com.google.common.collect.Maps;
import com.meituan.mafka.client.consumer.ConsumeStatus;

import java.util.Map;

/**
 * 屏蔽thrift、pigeon等接入方的差异性
 * @author lanxinyu
 */
public interface IClient {
    /**
     * default rpc client timeout
     */
    int DEFAULT_TIMEOUT = 1000;

    /**
     * 接口是否可用
     * @return
     */
    boolean ping();

    /**
     * 消费消息
     * @param bizAlias
     * @param data
     * @return
     */
    ConsumeStatus consume(String bizAlias, String data);

    class Cache {
        /**
         * 客户端复用 缓存
         */
        public static Map<String, IClient> CLIENT_ALIVE = Maps.newConcurrentMap();
    }
}
