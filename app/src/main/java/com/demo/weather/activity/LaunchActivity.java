package com.demo.weather.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.service.InitCityDataService;
import com.demo.weather.util.SharedPreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.demo.weather.config.SharePreferenceConfig.WEATHER_CITY_LIST_TAG;

/**
 * 启动页
 */
public class LaunchActivity extends BaseActivity {

    private static class MHandler extends Handler {
        private final WeakReference<LaunchActivity> mActivity;

        public MHandler(LaunchActivity activity) {
            mActivity = new WeakReference<LaunchActivity>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            LaunchActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.forward();
                        break;
                }
            }
        }
    }

    private MHandler handler = new MHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        startService(new Intent(this, InitCityDataService.class));

        handler.sendEmptyMessageDelayed(1, 500);

    }

    private void forward() {
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


}
