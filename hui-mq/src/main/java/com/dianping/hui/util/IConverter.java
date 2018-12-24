package com.dianping.hui.util;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.context.bean.ConnectionConfigStatusGroup;
import com.dianping.hui.context.bean.MafkaConnectionConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
/**
 * @author: lanxinyu@meituan.com  2018-11-21 4:56 PM
 * @Description:
 */
@Mapper
public interface IConverter {
    IConverter INSTANCE = Mappers.getMapper(IConverter.class);

    /**
     * 转化为mafka 客户端初始化所需要的类
     * @see ConnectionConfigEntity
     * @see MafkaConnectionConfig
     * @param entity
     * @return
     */
    MafkaConnectionConfig connectionConfigEntity2UniversalConnectionConfig(ConnectionConfigEntity entity);

    /**
     * 深拷贝
     * @param connectionConfigStatusGroup
     * @return
     */
    ConnectionConfigStatusGroup connectionConfigStatusGroupBackUp(ConnectionConfigStatusGroup connectionConfigStatusGroup);

    /**
     * 深拷贝
     * @param connectionConfigStatusGroup
     * @return
     */
    List<ConnectionConfigEntity> connectionConfigEntityListBackup(List<ConnectionConfigEntity> connectionConfigStatusGroup);

}
