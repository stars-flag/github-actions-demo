package com.example.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  {
 *       "fxDate": "2025-05-17",
 *       "sunrise": "05:17",
 *       "sunset": "18:43",
 *       "moonrise": "22:55",
 *       "moonset": "08:34",
 *       "moonPhase": "亏凸月",
 *       "moonPhaseIcon": "805",
 *       "tempMax": "36",
 *       "tempMin": "25",
 *       "iconDay": "101",
 *       "textDay": "多云",
 *       "iconNight": "305",
 *       "textNight": "小雨",
 *       "wind360Day": "0",
 *       "windDirDay": "北风",
 *       "windScaleDay": "1-3",
 *       "windSpeedDay": "3",
 *       "wind360Night": "0",
 *       "windDirNight": "北风",
 *       "windScaleNight": "1-3",
 *       "windSpeedNight": "3",
 *       "humidity": "95",
 *       "precip": "1.6",
 *       "pressure": "984",
 *       "vis": "18",
 *       "cloud": "65",
 *       "uvIndex": "11"
 *     }
 */
@ToString
@Getter
@Setter
public class QWeatherDay {

    // 日期
    private String fxDate;

    // 日出时间
    private String sunrise;

    // 日落时间
    private String sunset;

    // 当天最高温度
    private String tempMax;

    // 当天最低温度
    private String tempMin;

    // 白天天气状况文字描述
    private String textDay;

    // 晚间天气状况文字描述
    private String textNight;

    // 预报白天风向
    private String windDirDay;

    // 预报白天风力等级
    private String windScaleDay;

    // 预报白天风速，公里/小时
    private String windSpeedDay;

    //  预报夜间当天风向
    private String windDirNight;

    // 预报夜间风力等级
    private String windScaleNight;

    //预报夜间风速，公里/小时
    private String windSpeedNight;
}
