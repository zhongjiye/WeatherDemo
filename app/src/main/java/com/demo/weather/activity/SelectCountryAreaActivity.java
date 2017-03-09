package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.R;
import com.demo.weather.adapter.CountryAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.AppManager;
import com.demo.weather.util.CharacterParser;
import com.demo.weather.util.SharedPreferencesUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_CITYLIST;
import static com.demo.weather.config.SharePreferenceConfig.WEATHER_CITY_LIST_TAG;

/**
 * 选择国家
 */
public class SelectCountryAreaActivity extends BaseActivity implements AdapterView
    .OnItemClickListener, CountryAdapter.OnTagClickListener {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.listview)
    ListView mListview;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;


    private CountryAdapter mCountryAdapter;
    private Map<String, List<String>> mNameMap;
    private Map<String, WeatherCity> mAreaMap;
    private List<WeatherCity> mAreaList;
    private List<WeatherCity> mWeatherCityList;
    private List<String> mIndexList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        ButterKnife.inject(this);
        mTvTitle.setText(R.string.select_city);
        initData();
        initWeatherCityList();
        initGridView();
    }

    private void initData() {
        String datas = getIntent().getStringExtra("areaList");
        if (!TextUtils.isEmpty(datas)) {
            mAreaList = JSON.parseObject(datas, new TypeReference<List<WeatherCity>>() {
            });
            if (mAreaList != null && mAreaList.size() != 0) {

                mAreaMap = new HashMap<>();
                for (WeatherCity weatherCity : mAreaList) {
                    mAreaMap.put(weatherCity.getName(), weatherCity);
                }

                mNameMap = new TreeMap<>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });

                CharacterParser characterParser = CharacterParser.getInstance();

                for (WeatherCity temp : mAreaList) {
                    String name = temp.getName();
                    String c = characterParser.convert(name.substring(0, 1)).substring(0, 1);
                    List<String> list = mNameMap.get(c);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(name);
                    mNameMap.put(c, list);
                }
            }
        }
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


    private void initGridView() {
        mIndexList = new ArrayList<>();
        for (String temp : mNameMap.keySet()) {
            mIndexList.add(temp);
        }
        mCountryAdapter = new CountryAdapter(mContext, mIndexList, mNameMap, this);
        mListview.setAdapter(mCountryAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }


    @Override
    public void onTagClik(String name) {
        if (!TextUtils.isEmpty(name)) {
            WeatherCity weatherCity = mAreaMap.get(name);
            if (weatherCity != null) {
                mWeatherCityList.add(weatherCity);

                boolean flag = false;
                for (WeatherCity temp : mWeatherCityList) {
                    if (temp.isDefault()) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    WeatherCity temp = mWeatherCityList.get(0);
                    temp.setDefault(true);
                    mWeatherCityList.set(0, temp);
                }

                SharedPreferencesUtils.put(getApplicationContext(), WEATHER_CITY_LIST_TAG, JSON
                    .toJSONString(mWeatherCityList));
                sendBroadcast(new Intent(UPDATE_CITYLIST));

                if (!AppManager.getAppManager().isExisttActivity(MainActivity.class)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                AppManager.getAppManager().finishActivity(AddCityActivity.class);
                AppManager.getAppManager().finishActivity(SelectCountryActivity.class);
                finish();
            }
        }
    }
}
