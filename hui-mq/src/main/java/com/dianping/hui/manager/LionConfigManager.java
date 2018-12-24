package com.dianping.hui.manager;

import com.alibaba.fastjson.JSON;
import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.context.IConfigContext;
import com.dianping.hui.context.factory.IConfigManager;
import com.dianping.lion.Environment;
import com.dianping.lion.client.ConfigEvent;
import com.dianping.lion.client.ConfigListener;
import com.dianping.lion.client.Lion;
import com.dianping.lion.client.spring.LionConfig;
import com.dianping.lion.client.util.AuthUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 2:35 PM
 * @Description:
 */
@Component
@Slf4j
public class LionConfigManager implements IConfigManager, ConfigListener {

    private static final long FIRST = 1L;
    @Autowired
    private IConfigContext iConfigContext;
    @Autowired
    private RestTemplate restTemplate;
    @Setter
    @LionConfig("hui-mq.lion.config.uri")
    private String lionConfigUri;
    private LionHttpHelper lionHttpHelper;
    static final String KEY_CONNECTION_CONFIG = "hui-mq.connection.configs";
    static final String KEY_CONNECTION_CONFIG_TEST = "hui-mq.connection.configs.test";

    @PostConstruct
    public void init() {
        Lion.addConfigListener(this);
        this.lionHttpHelper = this.new LionHttpHelper();
    }
    @Override
    public ConnectionConfigEntity find(Long id) {
        List<ConnectionConfigEntity> all = findAll();
        if(CollectionUtils.isNotEmpty(all)) {
            for(ConnectionConfigEntity entity: all) {
                if(entity.getId().equals(id))
                    return entity;
            }
        }
        return null;
    }

    @Override
    public List<ConnectionConfigEntity> findAll() {
        return Lion.getList(KEY_CONNECTION_CONFIG, ConnectionConfigEntity.class);
    }

    @Override
    public boolean save(ConnectionConfigEntity entity) {
        if(entity.getId() != null)
            return update(entity);
        List<ConnectionConfigEntity> list = findAll();
        if(CollectionUtils.isEmpty(list)) {
            list = Lists.newArrayList();
            entity.setId(FIRST);
        } else {
            Optional<Long> maxId =
                    list.stream().map(l -> l.getId()).reduce(Long::max);
            entity.setId(maxId.get() + 1);
        }
        list.add(entity);
        return saveAll(list);
    }

    @Override
    public boolean saveAll(List<ConnectionConfigEntity> entityList) {
        if(entityList == null)
            entityList = Lists.newArrayList();
        return lionHttpHelper.save(entityList);
    }

    @Override
    public boolean delete(Long id) {
        if(id == null)
            return Boolean.FALSE;
        List<ConnectionConfigEntity> list = findAll();
        Iterator<ConnectionConfigEntity> iterator = list.iterator();
        if(iterator.hasNext()) {
            if(iterator.next().getId().equals(id))
                iterator.remove();
        }
        return lionHttpHelper.save(list);
    }

    @Override
    public boolean update(ConnectionConfigEntity entity) {
        if(entity.getId() == null)
            return save(entity);
        List<ConnectionConfigEntity> list = findAll();
        AtomicReference<ConnectionConfigEntity> backup = null;
        list.stream().filter(
                l -> entity.getId().equals(l.getId())
        ).findAny().ifPresent(
                old -> backup.set(old)
        );

        if(backup.get() != null)
            list.remove(backup.get());

        list.add(entity);
        return lionHttpHelper.save(list);
    }

    @Override
    public void configChanged(ConfigEvent configEvent) {
        if(KEY_CONNECTION_CONFIG.equals(configEvent.getKey())) {
            iConfigContext.refresh();
        }
    }

    class LionHttpHelper {

        static final String ACCOUNT = "hui-mq.lion.account";
        static final String PWD = "7RF1N7RK01";
        static final String URI_MULTI_SET = "/config2/multiSet";
        static final String CONNECTION_CONFIGS_DESC = "连接配置数组json";


        boolean save(List<ConnectionConfigEntity> entityList) {
            String date = AuthUtil.getAuthDate(new Date());
            String auth = AuthUtil.getAuthorization(URI_MULTI_SET, HttpMethod.POST.name(), date, ACCOUNT, PWD);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Date", date);
            headers.set("Authorization", auth);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<Lionet> request = new HttpEntity<>(
                    new Lionet(CONNECTION_CONFIGS_DESC, KEY_CONNECTION_CONFIG_TEST, JSON.toJSONString(entityList)), headers);
            try {
                return restTemplate.postForObject(lionConfigUri,request, Map.class).get("status").equals("success");
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }

    }
    @Setter
    @Getter
    class Lionet extends LinkedMultiValueMap<String, String> {
        final String env = Environment.getEnvironment();
        String desc;
        String key;
        String value;

        public Lionet(String desc, String key, String value) {
            this.desc = desc;
            this.key = key;
            this.value = value;
            super.add("env", env);
            super.add("desc", desc);
            super.add("key", key);
            super.add("value", value);
        }
    }
}