package com.example.job;

import com.example.api.QWResult;
import com.example.common.QWeatherConstant;
import com.example.config.prop.QWeatherProp;
import com.example.config.prop.WxProp;
import com.example.remote.QWeatherClient;
import com.example.remote.WxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WeatherPushTask {


    @Autowired

    private WxProp wxProp;

    @Autowired
    private QWeatherProp weatherProp;

    @Autowired
    private RestTemplate restTemplate;

    // @Scheduled(cron = "0 0 7 * * ?")
    public void pushWeather() {
        log.info("===============pushWeatherTask start===============");

        // 1. 获取token
        String token = WxClient.getToken(wxProp);

        // 2. 获取天气信息
        List<String> cityCodeList = weatherProp.getCityCodeList();
        List<String> cityNameList = weatherProp.getCityNameList();
        Assert.isTrue(cityCodeList.size() == cityNameList.size(), "城市数量不匹配");

        Map<String, QWResult> weathers = new HashMap<>();
        for (int i = 0; i < cityCodeList.size(); i++) {
            String cityCode = cityCodeList.get(i);
            String cityName = cityNameList.get(i);
            log.info("获取{}天气信息", cityName);
            QWResult weather = QWeatherClient.getWeather(weatherProp, cityCode, QWeatherConstant.PATH_HOURS);
            log.info("{}天气信息:{}", cityName, weather);
            weathers.put(cityName, weather);
        }

        // 3. 获取用户消息
        List<String> userList = WxClient.getUsers(token);

        // 4. 推送天气信息
        for (String user : userList) {
            log.info("推送{}天气信息", user);
            WxClient.pushWeather(token, user, wxProp.getTmplId(), weathers);
        }

        log.info("===============pushWeatherTask end===============");
    }


}
