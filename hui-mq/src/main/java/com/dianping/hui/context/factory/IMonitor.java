package com.dianping.hui.context.factory;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.context.bean.ConnectionConfigStatusGroup;

import java.util.List;
/**
 * @author lanxinyu
 * 监控器
 */
public interface IMonitor {

    /**
     * 获取上次变更时备份的原始配置数据
     * @return
     */
    List<ConnectionConfigEntity> originalBackupConfig();

    /**
     * 获取当前的原始配置数据
     * @return
     */
    List<ConnectionConfigEntity> originalCurrentConfig();

    /**
     * 获取上次变更时备份的原始配置数据 聚合后的备份数据
     * @return
     */
    ConnectionConfigStatusGroup backupConfig();
    /**
     * 获取当前的原始配置数据 聚合后的配置数据
     * @return
     */
    ConnectionConfigStatusGroup currentConfig();
}
