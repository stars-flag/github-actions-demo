package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxPushTmpl<T> {

    private String touser;

    @JsonProperty("template_id")
    private String  templateId;

    private T data;
}
