package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.R;
import com.demo.weather.adapter.CityAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.AppManager;
import com.demo.weather.util.AssetsUtil;
import com.demo.weather.util.SharedPreferencesUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
public class AddCityActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.gv_city)
    GridView mGvCity;


    private static class MHandler extends Handler {
        private final WeakReference<AddCityActivity> mActivity;

        public MHandler(AddCityActivity activity) {
            mActivity = new WeakReference<AddCityActivity>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            AddCityActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
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
                String datas = AssetsUtil.getAssetFile(AddCityActivity.this, "citys.json");
                if (!TextUtils.isEmpty(datas)) {
                    mProvinceMap = JSON.parseObject(datas,
                        new TypeReference<Map<String, Map<String, List<WeatherCity>>>>() {
                        });
                    for (String temp : mProvinceMap.keySet()) {
                        mProvinceList.add(temp);
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void initGridView() {
        mProvinceList = new ArrayList<>();
        mProvinceAdapter = new CityAdapter(this, mProvinceList);
        mGvCity.setAdapter(mProvinceAdapter);
        mGvCity.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String province = mProvinceList.get(position);
        if (!TextUtils.isEmpty(province)) {
            Map<String, List<WeatherCity>> cityMap = mProvinceMap.get(province);
            if (cityMap != null && cityMap.size() != 0) {
                Intent intent = new Intent(this, SelectCityActivity.class);
                intent.putExtra("cityMap", JSON.toJSONString(cityMap));
                intent.putExtra("province", province);
                startActivity(intent);
            }
        }
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
