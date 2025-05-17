package com.example.config;

import com.example.config.prop.QWeatherProp;
import com.example.config.prop.WxProp;
import com.example.utils.SpringContextHolder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @ConfigurationProperties(prefix = "wx")
    @Bean
    public WxProp wxProp() {
        return new WxProp();
    }

    @ConfigurationProperties(prefix = "qweather")
    @Bean
    public QWeatherProp qweatherProp() {
        return new QWeatherProp();
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .setMaxConnPerRoute(200)
                .setMaxConnTotal(50)
                .setConnectionTimeToLive(10L, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient());
        RestTemplate restTemplate = new RestTemplate(factory);
 /*       List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();
        boolean add = messageConverterList.add(new MappingJackson2HttpMessageConverter());
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();*/

        return restTemplate;
    }
}
