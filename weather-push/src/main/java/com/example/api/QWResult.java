package com.example.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class QWResult {

    private String code;

    private List<QWeatherDay> daily;


    private List<QWeatherHour> hourly;
}
