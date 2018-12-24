package com.dianping.hui.context.bean;

import com.dianping.hui.bean.ClientType;
import lombok.Getter;

import static com.dianping.hui.context.bean.ConnectionConfigStatusGroup.Helper.*;

/**
 * @author: lanxinyu@meituan.com  2018-11-27 11:52 AM
 * @Description:
 */
@Getter
public class RpcConnectionConfig {
    /**
     * thrift config
     */
    private String rpcAppkey;
    private Integer rpcPort;
    /**
     * pigeon config
     */
    private String rpcUrl;
    /**
     * common config
     */
    private ClientType rpcType;
    private Integer rpcTimeout;

    public RpcConnectionConfig setKey(String key) {
        String[] array = analyseKey(key);
        this.rpcAppkey = analyseRpcAppkey(array);
        this.rpcPort   = analyseRpcPort(array);
        this.rpcUrl    = analyseRpcUrl(array);
        this.rpcTimeout = analyseRpcTimeout(array);
        this.rpcType   = analyseRpcType(array);
        return this;
    }
}
