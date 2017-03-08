package com.demo.weather.service;

import com.demo.weather.util.CityDataUtil;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by zhongjy on 2017/3/8.
 */

public class InitCityDataService extends IntentService {

    public InitCityDataService() {
        super("com.demo.weather.service.InitCityDataService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CityDataUtil.getCityMap(getApplicationContext());
        CityDataUtil.getForeignCityMap(getApplicationContext());
    }
}
