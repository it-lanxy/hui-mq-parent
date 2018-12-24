package com.dianping.hui.manager;

import com.dianping.hui.context.bean.ConnectionStatusMark;
import com.dianping.hui.context.factory.IConnectionOperationFlowManager;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lanxinyu@meituan.com  2018-11-26 8:33 PM
 * @Description: 内存存储 {@link ConnectionStatusMark}操作流
 */
@Component
public class RAMOperationFlowManager implements IConnectionOperationFlowManager {
    private static final int BALANCE_SIZE = 100;
    private static final int BOUNDARY = 80;
    private List<ConnectionStatusMark> WORK_FLOW = Lists.newCopyOnWriteArrayList();

    @Override
    public IConnectionOperationFlowManager append(ConnectionStatusMark connectionStatusMark) {
        if(neeDataArchiving())
            dataArchiving();
        WORK_FLOW.add(connectionStatusMark);
        return this;
    }

    @Override
    public boolean neeDataArchiving() {
        return WORK_FLOW.size() > BALANCE_SIZE;
    }

    @Override
    public void dataArchiving() {
        WORK_FLOW = WORK_FLOW.subList(BOUNDARY, BALANCE_SIZE);
    }

    @Override
    public List<ConnectionStatusMark> display() {
        return WORK_FLOW;
    }

}
