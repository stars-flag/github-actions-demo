package com.example.common;

public class QWeatherConstant {

    private QWeatherConstant() {
    }

    public static final String LOCATION = "location";


    /**
     * curl -X GET --compressed \
     * -H 'Authorization: Bearer your_token' \
     * 'https://your_api_host/v7/weather/24h?location=101010100'
     */
    public static final String PATH_HOURS = "v7/weather/24h";

    /**
     * curl -X GET --compressed \
     * -H 'Authorization: Bearer your_token' \
     * 'https://your_api_host/v7/weather/3d?location=101010100'
     */
    public static final String PATH_3DAYS = "v7/weather/3d";




}
