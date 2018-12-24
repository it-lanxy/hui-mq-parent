package com.dianping.hui.context.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author: lanxinyu@meituan.com  2018-11-22 5:03 PM
 * @Description:
 */
@Getter
@Setter
public class ConnectionConfig {
    private RpcConnectionConfig rpcConnectionConfig;
    private Set<MafkaConnectionConfig> mafkaConnectionConfigSet;
    private Set<Long> ids;
}
