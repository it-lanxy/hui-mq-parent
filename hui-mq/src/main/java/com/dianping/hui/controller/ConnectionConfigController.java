package com.dianping.hui.controller;

import com.alibaba.fastjson.JSON;
import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.bean.IClient;
import com.dianping.hui.manager.MafkaConsumerManager;
import com.dianping.hui.service.ConnectionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lanxinyu@meituan.com  2018-11-21 4:53 PM
 * @Description:
 */
@RestController
public class ConnectionConfigController {
    @Autowired
    private ConnectionConfigService service;

    @PostMapping("/save")
    public String save (@RequestBody ConnectionConfigEntity entity){
        service.saveOrUpdate(entity);
        return "success";
    }

    @GetMapping("/deploy/{id}")
    public String deploy (@PathVariable Long id){
        service.deploy(id);
        return "success";
    }

    @GetMapping("/online_config")
    public String onlineConfig (){
        return JSON.toJSONString(service.getOnlineConfig());
    }

    @GetMapping("/offline_config")
    public String offlineConfig (){
        return JSON.toJSONString(service.getOfflineConfig());
    }

    @GetMapping("/online_client")
    public String onlineIClient(){
        Map<String, String> result = new HashMap<>();
        IClient.Cache.CLIENT_ALIVE.entrySet().forEach(
                entry ->
                    result.put(entry.getKey(),entry.getValue().toString())
        );
        return JSON.toJSONString(result);
    }

    @GetMapping("/online_consumer")
    public String onlineIconsumer(){
        Map<Long, String> result = new HashMap<>();
        MafkaConsumerManager.CONSUMER_ALIVE.entrySet().forEach(
                entry ->
                        result.put(entry.getKey(),entry.getValue().toString())
        );
        return JSON.toJSONString(result);
    }

    @GetMapping("/operationflow")
    public String operationflow (){
        return JSON.toJSONString(service.getOperationFlow());
    }

}
