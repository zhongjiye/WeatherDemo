package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.R;
import com.demo.weather.adapter.CityAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.AppManager;
import com.demo.weather.util.SharedPreferencesUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_CITYLIST;
import static com.demo.weather.config.SharePreferenceConfig.WEATHER_CITY_LIST_TAG;

/**
 * 选择地区
 */
public class SelectAreaActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_city)
    TextView mTvCity;
    @InjectView(R.id.gv_area)
    GridView mGvArea;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;

    private String mCity;
    private CityAdapter mAreaAdapter;
    private List<WeatherCity> mAreaList;
    private List<WeatherCity> mWeatherCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        ButterKnife.inject(this);
        mTvTitle.setText(R.string.select_area);
        initData();
        initGridView();
        initWeatherCityList();
    }

    private void initData() {
        String datas = getIntent().getStringExtra("areaList");
        mCity = getIntent().getStringExtra("area");
        if (!TextUtils.isEmpty(datas)) {
            mAreaList = JSON.parseObject(datas, new TypeReference<List<WeatherCity>>() {
            });
        }
    }

    private void initGridView() {
        mTvCity.setText(TextUtils.isEmpty(mCity) ? "" : mCity);
        mAreaAdapter = new CityAdapter(mAreaList, this);
        mGvArea.setAdapter(mAreaAdapter);
        mGvArea.setOnItemClickListener(this);
    }

    private void initWeatherCityList() {
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

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mWeatherCityList.add(mAreaList.get(position));
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
        AppManager.getAppManager().finishActivity(AddCityActivity.class);
        AppManager.getAppManager().finishActivity(SelectCityActivity.class);
        finish();
    }
}
