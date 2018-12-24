package com.dianping.hui.context.bean;

import lombok.Data;

/**
 * @author: lanxinyu@meituan.com  2018-11-27 11:43 AM
 * @Description:
 */

@Data
public class MafkaConnectionConfig {
    /**
     * mafka config
     */
    private String mfkBgNameSpace;
    private String mfkClientAppkey;
    private String mfkSubscribeGroup;
    private String mfkTopic;
    /**
     * biz config
     */
    private String bizAlias;
    /**
     * common config
     */
    private Boolean isPublish;
    private Long id;
}