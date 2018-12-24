package com.dianping.hui.context.support;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.bean.SourceType;
import com.dianping.hui.context.bean.ConnectionConfigStatusGroup;
import com.dianping.hui.context.factory.IConfigManager;
import com.dianping.hui.context.factory.IConnectionOperationFlowManager;
import com.dianping.hui.context.factory.IMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lanxinyu@meituan.com  2018-11-28 12:00 PM
 * @Description: 通常的配置
 */
@Component
@Slf4j
public class GenericConfigContext extends AbstractConfigContext {

    @Override
    public IConfigManager getConfigManager() {
        return factory.pickIConfigManager(SourceType.LION);
    }

    /**
     * 若不需要备份
     * @see GenericConfigContext##getConfigManager
     * 配置相同即可
     *
     * @return
     * @see com.dianping.hui.context.backup.BackupAdvice
     */
    @Override
    public IConfigManager getBackupConfigManager() {
        return factory.pickIConfigManager(SourceType.LION);
    }

    @Override
    public IConnectionOperationFlowManager getConnectionOperationFlowManager() {
        return factory.pickIConnectionOperationFlowManager(SourceType.RAM);
    }
    @Override
    public IMonitor getMonitor() {
        return new IMonitor() {
            @Override
            public List<ConnectionConfigEntity> originalBackupConfig() {
                return originalBackupConfig;
            }

            @Override
            public List<ConnectionConfigEntity> originalCurrentConfig() {
                return originalCurrentConfig;
            }

            @Override
            public ConnectionConfigStatusGroup backupConfig() {
                return backupConfig;
            }

            @Override
            public ConnectionConfigStatusGroup currentConfig() {
                return currentConfig;
            }
        };
    }

    @Override
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void watchdogTimer() {
        refresh();
    }
}
