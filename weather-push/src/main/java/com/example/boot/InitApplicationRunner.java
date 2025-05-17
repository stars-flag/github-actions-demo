package com.example.boot;

import com.example.job.WeatherPushTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {

    @Autowired
    private WeatherPushTask weatherPushTask;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        weatherPushTask.pushWeather();
    }
}
