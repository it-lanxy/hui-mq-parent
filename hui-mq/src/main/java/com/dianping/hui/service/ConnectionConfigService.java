package com.dianping.hui.service;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.context.IConfigContext;
import com.dianping.hui.context.bean.ConnectionConfig;
import com.dianping.hui.context.bean.ConnectionStatusMark;
import com.dianping.hui.context.factory.IConfigManager;
import com.dianping.hui.context.factory.IConnectionOperationFlowManager;
import com.dianping.hui.context.factory.IMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: lanxinyu@meituan.com  2018-11-21 4:56 PM
 * @Description:
 */
@Service
public class ConnectionConfigService{

    private IMonitor monitor;
    private IConnectionOperationFlowManager connectionOperationFlowManager;
    private IConfigContext configContext;
    private IConfigManager configManager;

    @Autowired public ConnectionConfigService(IConfigContext configContext) {
        this.configContext = configContext;
        this.configManager = configContext.getConfigManager();
        this.monitor = configContext.getMonitor();
        this.connectionOperationFlowManager = configContext.getConnectionOperationFlowManager();
    }


    public boolean saveOrUpdate(ConnectionConfigEntity entity) {
        if(entity.getId() != null)
            if(configManager.find(entity.getId()) != null) {
                return configManager.update(entity);
            }
        return configManager.save(entity);
    }

    public ConnectionConfigEntity deploy(Long id) {
        ConnectionConfigEntity entity = configManager.find(id);
        if(entity != null)
            configContext.refresh();
        return entity;
    }

    public Map<String, ConnectionConfig> getOnlineConfig() {
        return monitor.currentConfig().getOnlineConfig();
    }

    public Map<String, ConnectionConfig> getOfflineConfig() {
        return monitor.currentConfig().getOfflineConfig();
    }

    public List<ConnectionStatusMark> getOperationFlow() {
        return connectionOperationFlowManager.display();
    }
}
