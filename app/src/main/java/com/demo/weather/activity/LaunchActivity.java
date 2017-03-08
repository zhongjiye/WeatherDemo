package com.demo.weather.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.SharedPreferencesUtils;

import java.util.List;

import static com.demo.weather.config.SharePreferenceConfig.WEATHER_CITY_LIST_TAG;

/**
 * 启动页
 */
public class LaunchActivity extends BaseActivity {


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
