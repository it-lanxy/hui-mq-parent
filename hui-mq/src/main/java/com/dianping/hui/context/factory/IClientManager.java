package com.dianping.hui.context.factory;

import com.dianping.hui.bean.IClient;
import com.dianping.hui.context.bean.RpcConnectionConfig;

/**
 * @author lanxinyu
 */
public interface IClientManager {
    /**
     * 创建客户端
     * @param entity
     * @return
     */
    IClient create(RpcConnectionConfig entity);
}
