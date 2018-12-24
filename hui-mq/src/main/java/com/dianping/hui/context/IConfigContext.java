package com.dianping.hui.context;

import com.dianping.hui.context.factory.IConfigManager;
import com.dianping.hui.context.factory.IConnectionOperationFlowManager;
import com.dianping.hui.context.factory.IMonitor;

/**
 * @author: lanxinyu@meituan.com  2018-11-27 7:24 PM
 * @Description:
 */
public interface IConfigContext {

    /**
     * 刷新容器中维护的consumer、iclient实例
     */
    void refresh();

    /**
     * 获取配置 管理器
     * @return
     */
    IConfigManager getConfigManager();

    /**
     * 获取操作实例动作及其结果的流水 管理器
     * @see com.dianping.hui.context.bean.ActionType
     * @return
     */
    IConnectionOperationFlowManager getConnectionOperationFlowManager();

    /**
     * 监控器：获取容器consumer、iclient实例的状态及配置等的监控数据
     * @return
     */
    IMonitor getMonitor();
}
