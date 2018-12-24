package com.dianping.hui.context.factory;

import com.dianping.hui.bean.ConnectionConfigEntity;

import java.util.List;
/**
 * @author lanxinyu
 * 数据源 操作管理器
 */
public interface IConfigManager {

    /**
     * 查找
     * @param id
     * @return
     */
    ConnectionConfigEntity find(Long id);

    /**
     * 查找所有
     * @return
     */
    List<ConnectionConfigEntity> findAll();

    /**
     * 保存
     * @param entity
     * @return
     */
    boolean save(ConnectionConfigEntity entity);

    /**
     * 保存所有
     * @param entityList
     * @return
     */
    boolean saveAll(List<ConnectionConfigEntity> entityList);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 更新
     * @param entity
     * @return
     */
    boolean update(ConnectionConfigEntity entity);
}
