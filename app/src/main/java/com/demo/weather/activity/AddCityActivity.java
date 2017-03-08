package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.adapter.CityAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.cusview.MyGridView;
import com.demo.weather.util.AppManager;
import com.demo.weather.util.CityDataUtil;
import com.demo.weather.util.SharedPreferencesUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.config.SharePreferenceConfig.WEATHER_CITY_LIST_TAG;

/**
 * 添加城市
 */
public class AddCityActivity extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.china_city_gridview)
    MyGridView mChinaCityGridview;
    @InjectView(R.id.foreign_city_gridview)
    MyGridView mForeignCityGridview;


    private static class MHandler extends Handler {
        private final WeakReference<AddCityActivity> mActivity;

        public MHandler(AddCityActivity activity) {
            mActivity = new WeakReference<AddCityActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddCityActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        for (String temp : activity.mProvinceMap.keySet()) {
                            activity.mProvinceList.add(temp);
                        }
                        for (String temp : activity.mStateMap.keySet()) {
                            activity.mStateList.add(temp);
                        }
                        activity.mProvinceAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    private List<WeatherCity> mWeatherCityList;
    private List<String> mProvinceList;
    private CityAdapter mProvinceAdapter;
    private Map<String, Map<String, List<WeatherCity>>> mProvinceMap;

    private List<String> mStateList;
    private CityAdapter mStateAdapter;
    private Map<String, Map<String, List<WeatherCity>>> mStateMap;


    private MHandler handler = new MHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ButterKnife.inject(this);
        initCityData();
        initGridView();
    }

    private void initCityData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mProvinceMap = CityDataUtil.getCityMap(mContext);
                mStateMap = CityDataUtil.getForeignCityMap(mContext);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void initGridView() {
        mProvinceList = new ArrayList<>();
        mProvinceAdapter = new CityAdapter(this, mProvinceList);
        mChinaCityGridview.setAdapter(mProvinceAdapter);
        mChinaCityGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String province = mProvinceList.get(position);
                if (!TextUtils.isEmpty(province)) {
                    Map<String, List<WeatherCity>> cityMap = mProvinceMap.get(province);
                    if (cityMap != null && cityMap.size() != 0) {
                        Intent intent = new Intent(AddCityActivity.this, SelectCityActivity.class);
                        intent.putExtra("cityMap", JSON.toJSONString(cityMap));
                        intent.putExtra("province", province);
                        startActivity(intent);
                    }
                }
            }
        });
        mStateList = new ArrayList<>();
        mStateAdapter = new CityAdapter(this, mStateList);
        mForeignCityGridview.setAdapter(mStateAdapter);
        mForeignCityGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String state = mStateList.get(position);
                if (!TextUtils.isEmpty(state)) {
                    Map<String, List<WeatherCity>> countryMap = mStateMap.get(state);
                    if (countryMap != null && countryMap.size() != 0) {
                        Intent intent = new Intent(AddCityActivity.this, SelectCountryActivity
                            .class);
                        intent.putExtra("countryMap", JSON.toJSONString(countryMap));
                        startActivity(intent);
                    }
                }
            }
        });
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                close();
                break;
        }
    }

    private void close() {

        mWeatherCityList = new ArrayList<>();
        String content = SharedPreferencesUtils.get(getApplicationContext(),
            WEATHER_CITY_LIST_TAG, "").toString();
        if (!TextUtils.isEmpty(content)) {
            List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
            if (list != null && list.size() != 0) {
                mWeatherCityList.addAll(list);
            }
        }

        if (mWeatherCityList.size() == 0) {
            Toast.makeText(this, "请至少添加一个城市", Toast.LENGTH_LONG).show();
        } else {
            if (!AppManager.getAppManager().isExisttActivity(MainActivity.class)) {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        close();
    }

}
