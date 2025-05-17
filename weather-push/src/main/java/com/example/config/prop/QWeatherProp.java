package com.example.config.prop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Setter
@Getter
public class QWeatherProp implements InitializingBean {


    private String cityNames;

    private String cityCodes;

    private String host;

    private String privateKey;

    private String keyId;

    private String projectId;

    private List<String> cityNameList;

    private List<String> cityCodeList;

    @Override
    public void afterPropertiesSet() throws Exception {
        cityNameList = Arrays.asList(cityNames.split(","));
        cityCodeList = Arrays.asList(cityCodes.split(","));
        log.info("城市名列表{}", cityNameList);
        log.info("城市Code列表{}", cityCodeList);
    }
}
