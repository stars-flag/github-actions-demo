package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 正常情况下，微信会返回下述JSON数据包给公众号：
 * <p>
 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
 */
@Setter
@Getter
public class WXTokenDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;
}
