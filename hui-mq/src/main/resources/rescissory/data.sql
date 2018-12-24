insert into connection_config_entity
       (id,mfk_bg_name_space,mfk_client_appkey,mfk_subscribe_group,mfk_topic,is_publish,biz_alias, rpc_appkey,rpc_port,rpc_url,rpc_type,rpc_timeout)
values (1,'jinrong','mopay-service','mopay-service','payOrder_qa', true ,'i_pay',null,null,'com.dianping.mopay.mq.MafkaConsumer', 0, 1000);
insert into connection_config_entity
       (id,mfk_bg_name_space,mfk_client_appkey,mfk_subscribe_group,mfk_topic,is_publish,biz_alias, rpc_appkey,rpc_port,rpc_url,rpc_type,rpc_timeout)
values (2,'pingtai','mopay-service','mopay-service','ugc_mt_recommend_delete', true ,'ugc_recommend',null,null,'com.dianping.mopay.mq.MafkaConsumer', 0, 1000);
insert into connection_config_entity
       (id,mfk_bg_name_space,mfk_client_appkey,mfk_subscribe_group,mfk_topic,is_publish,biz_alias, rpc_appkey,rpc_port,rpc_url,rpc_type,rpc_timeout)
values (3,'daocan','mopay-service','mopay-service','maiton.order.notify', true ,'order_notify',null,null,'com.dianping.mopay.mq.MafkaConsumer', 0, 1000);

/*insert into connection_config_entity
       (id,mfk_bg_name_space,mfk_client_appkey,mfk_subscribe_group,mfk_topic,is_publish,biz_alias, rpc_appkey,rpc_port,rpc_url,rpc_type,rpc_timeout)
values (1,'daocan','hui-open-business-web','hui-open-business-web','maiton.order.push',true ,'买单.订单.推送','maiton-classic-mapi-web',8000,'',1, 1000);*/

