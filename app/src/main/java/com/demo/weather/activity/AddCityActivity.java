package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.AppManager;
import com.demo.weather.util.SharedPreferencesUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_CITYLIST;

/**
 * 添加城市
 */
public class AddCityActivity extends BaseActivity {

    public static final String WEATHER_CITY_LIST_TAG = "weather_city_list";
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_shanghai)
    TextView mTvShanghai;
    @InjectView(R.id.tv_beijing)
    TextView mTvBeijing;
    @InjectView(R.id.tv_nanjing)
    TextView mTvNanjing;
    @InjectView(R.id.tv_hefei)
    TextView mTvHefei;
    @InjectView(R.id.tv_add_all)
    TextView mTvAddAll;
    @InjectView(R.id.activity_main)
    LinearLayout mActivityMain;

    private List<WeatherCity> mWeatherCityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        mWeatherCityList = new ArrayList<>();
        String content = SharedPreferencesUtils.get(getApplicationContext(),
            WEATHER_CITY_LIST_TAG, "").toString();
        if (!TextUtils.isEmpty(content)) {
            List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
            if (list != null && list.size() != 0) {
                mWeatherCityList.addAll(list);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_shanghai, R.id.tv_beijing, R.id.tv_nanjing, R.id.tv_hefei, R.id.tv_add_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mWeatherCityList.size() == 0) {
                    Toast.makeText(this, "请至少添加一个城市", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                break;
            case R.id.tv_shanghai:
                mWeatherCityList.add(new WeatherCity(true, true, "上海", new Weather("多云转晴", "a20", 5, 18,
                    6, "多云", 2, "西北风", 1025, 141, "2017-02-23", 25, "05:35",
                    "17:32", "弱")));
                close();
                break;
            case R.id.tv_beijing:
                mWeatherCityList.add(new WeatherCity(false, false, "北京", new Weather("晴", "a32", 16, 30, 6,
                    "晴", 1, "北风", 1047, 40, "2017-02-23", 25, "06:05",
                    "17:56", "强")));
                close();
                break;
            case R.id.tv_nanjing:
                mWeatherCityList.add(new WeatherCity(false, false, "南京", new Weather("小雪", "a13", -5, 4, 6,
                    "小雪", 1, "东风", 1025, 200, "2017-02-23", 25, "06:35",
                    "18:32", "较弱")));
                close();
                break;
            case R.id.tv_hefei:
                mWeatherCityList.add(new WeatherCity(false, false, "合肥", new Weather("小雪", "a13", -5, 4, 6,
                    "小雪", 1, "东风", 1025, 200, "2017-02-23", 25, "06:35",
                    "18:32", "较弱")));
                close();
                break;
            case R.id.tv_add_all:
                mWeatherCityList.add(new WeatherCity(true, true, "上海", new Weather("多云转晴", "a20", 5, 18,
                    6, "多云", 2, "西北风", 1025, 141, "2017-02-23", 25, "05:35",
                    "17:32", "弱")));
                mWeatherCityList.add(new WeatherCity(false, false, "北京", new Weather("晴", "a32", 16, 30, 6,
                    "晴", 1, "北风", 1047, 40, "2017-02-23", 25, "06:05",
                    "17:56", "强")));
                mWeatherCityList.add(new WeatherCity(false, false, "南京", new Weather("小雪", "a13", -5, 4, 6,
                    "小雪", 1, "东风", 1025, 200, "2017-02-23", 25, "06:35",
                    "18:32", "较弱")));
                mWeatherCityList.add(new WeatherCity(false, false, "合肥", new Weather("小雪", "a13", -5, 4, 6,
                    "小雪", 1, "东风", 1025, 200, "2017-02-23", 25, "06:35",
                    "18:32", "较弱")));
                close();
                break;
        }
    }

    private void close() {

        boolean flag = false;
        for (WeatherCity temp : mWeatherCityList) {
            if (temp.isDefault()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            WeatherCity weatherCity = mWeatherCityList.get(0);
            weatherCity.setDefault(true);
            mWeatherCityList.set(0, weatherCity);
        }

        SharedPreferencesUtils.put(getApplicationContext(), WEATHER_CITY_LIST_TAG, JSON
            .toJSONString(mWeatherCityList));
        sendBroadcast(new Intent(UPDATE_CITYLIST));
        if (!AppManager.getAppManager().isExisttActivity(MainActivity.class)) {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mWeatherCityList.size() == 0) {
            Toast.makeText(this, "请至少添加一个城市", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
