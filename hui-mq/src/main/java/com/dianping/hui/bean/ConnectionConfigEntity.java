package com.dianping.hui.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author: lanxinyu@meituan.com  2018-11-21 4:38 PM
 * @Description:
 */
@Entity
@Data
public class ConnectionConfigEntity {
    /**
     * mafka config
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String mfkBgNameSpace;
    @Column
    private String mfkClientAppkey;
    @Column
    private String mfkSubscribeGroup;
    @Column
    private String mfkTopic;
    @Column
    private Boolean isPublish;
    @Column
    private String bizAlias;
    /**
     * thrift config
     */
    @Column
    private String rpcAppkey;
    @Column
    private Integer rpcPort;
    /**
     * pigeon config
     */
    @Column
    private String rpcUrl;
    /**
     * common config
     */
    @Column
    private ClientType rpcType;
    @Column
    private Integer rpcTimeout;

}
