package com.demo.weather.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页
 */
public class LaunchActivity extends BaseActivity {

    public static final String WEATHER_CITY_LIST_TAG = "weather_city_list";

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String content = SharedPreferencesUtils.get(getApplicationContext(),
                    WEATHER_CITY_LIST_TAG, "").toString();
                if (!TextUtils.isEmpty(content)) {
                    List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
                    if (list != null && list.size() != 0) {
                        startActivity(new Intent(mContext, MainActivity.class));
                    } else {
                        startActivity(new Intent(mContext, AddCityActivity.class));
                    }
                } else {
                    startActivity(new Intent(mContext, AddCityActivity.class));
                }
                finish();
            }
        }, 500);
    }

}
