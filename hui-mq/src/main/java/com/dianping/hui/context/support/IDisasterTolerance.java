package com.dianping.hui.context.support;

import com.dianping.hui.context.factory.IConfigManager;

/**
 * @author: lanxinyu@meituan.com  2018-11-29 2:38 PM
 * @Description: 灾备配置
 */
public interface IDisasterTolerance {
    /**
     * 配置的容灾备份 管理者
     * @return
     */
    IConfigManager getBackupConfigManager();

    /**
     * 容器运行时检查掉线consumer并及时唤起
     */
    void watchdogTimer();
}
