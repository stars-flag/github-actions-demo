package com.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Slf4j
public class RestClient {

    private static final RestTemplate restTemplate;

    static {
        restTemplate = SpringContextHolder.getBean(RestTemplate.class);
    }

    public static <T> T get(String url, String path, HttpHeaders headers, MultiValueMap<String, String> queryMaps, Class<T> clazz) {
        URI uri = UrlHelper.buildUri(url, path, queryMaps);
        try {

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<T> tokenEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
            if (HttpStatus.OK.equals(tokenEntity.getStatusCode())) {
                return tokenEntity.getBody();
            } else {
                log.error("GET: {}, Result:{}", uri, tokenEntity);
            }
        } catch (Exception e) {
            log.error("GET: {}", uri, e);
        }
        return null;
    }

    public static <T> T postJson(String url, String path, HttpHeaders headers, MultiValueMap<String, String> queryMaps, Object json, Class<T> clazz) {
        URI uri = UrlHelper.buildUri(url, path, queryMaps);
        try {
            headers = Optional.ofNullable(headers).orElse(new HttpHeaders());

            ObjectMapper  mapper = new ObjectMapper();
            System.out.println(mapper.writeValueAsString(json));

            HttpEntity<Object> entity = new HttpEntity<>(json, headers);
            ResponseEntity<T> tokenEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, clazz);
            if (HttpStatus.OK.equals(tokenEntity.getStatusCode())) {
                return tokenEntity.getBody();
            } else {
                log.error("POST: {}, Result:{}", uri, tokenEntity);
            }
        } catch (Exception e) {
            log.error("GET: {}", uri, e);
        }
        return null;
    }
}
