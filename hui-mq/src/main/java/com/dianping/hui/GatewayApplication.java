package com.dianping.hui;

import com.dianping.lion.client.spring.LionPlaceholderConfigurer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author: lanxinyu@meituan.com  2018-11-21 4:32 PM
 * @Description:
 */
@SpringBootApplication
@PropertySource(value = "classpath:META-INF/app.properties")
@EnableScheduling
public class GatewayApplication extends SpringBootServletInitializer {

    @Bean public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    @Bean public LionPlaceholderConfigurer lionPlaceholderConfigurer(){
        return new LionPlaceholderConfigurer();
    }
}
