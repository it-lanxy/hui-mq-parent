package com.dianping.hui.mq.pigeon;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 1:37 PM
 * @Description:
 */
public interface IConsumerGateway {
    boolean ping();
    int consume(String bizAlias, String data);
}
