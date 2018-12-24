package com.dianping.hui.context.factory;

import com.dianping.hui.context.bean.ConnectionStatusMark;

import java.util.List;
/**
 * @author lanxinyu
 * 操作连接后的结果状态 管理器
 */
public interface IConnectionOperationFlowManager {
    /**
     * 追加 操作连接后的结果状态
     * @param connectionStatusMark
     * @return
     */
    IConnectionOperationFlowManager append(ConnectionStatusMark connectionStatusMark);

    /**
     * 是否需要数据归档
     * @return
     */
    boolean neeDataArchiving();

    /**
     * 数据归档
     */
    void dataArchiving();

    /**
     * 展示操作连接后的结果状态
     * @return
     */
    List<ConnectionStatusMark> display();
}
