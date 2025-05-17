package com.example.utils;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class UrlHelper {

    public static URI buildUri(String url, String path, MultiValueMap<String, String> queryMaps) {
        return UriComponentsBuilder
                .fromHttpUrl(url)
                .path(path)
                .queryParams(queryMaps)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

    }
}
