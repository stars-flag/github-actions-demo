package com.example.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * {
 * "fxTime": "2025-05-17T17:00+08:00",
 * "temp": "23",
 * "icon": "100",
 * "text": "晴",
 * "wind360": "311",
 * "windDir": "西北风",
 * "windScale": "3-4",
 * "windSpeed": "25",
 * "humidity": "15",
 * "pop": "0",
 * "precip": "0.0",
 * "pressure": "1006",
 * "cloud": "86",
 * "dew": "-5"
 * }
 */
@ToString
@Setter
@Getter
public class QWeatherHour {

    private Date fxTime;

    private String text;

    private String temp;

    private String windDir;

    private String windScale;

    private String windSpeed;


    public String toLine() {
       // return String.format("天气:%s, 气温(°):%s, 风向:%s, 风力等级:%s, 风速(公里/小时):%s", text, temp, windDir, windScale, windSpeed);
        // 微信有字数限制
        return String.format("%s, %s°", text, temp);
    }
}
