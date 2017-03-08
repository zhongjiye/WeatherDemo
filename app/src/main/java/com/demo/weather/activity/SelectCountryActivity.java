package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.R;
import com.demo.weather.adapter.CityAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.CharacterParser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 选择国家
 */
public class SelectCountryActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.listview)
    ListView mListview;


    private CityAdapter mCityAdapter;
    private Map<String, List<String>> mNameMap;
    private Map<String, List<WeatherCity>> countryMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        ButterKnife.inject(this);
        initData();
        initGridView();
    }

    private void initData() {
        String datas = getIntent().getStringExtra("countryMap");
        if (!TextUtils.isEmpty(datas)) {
            countryMap = JSON.parseObject(datas, new TypeReference<Map<String, List<WeatherCity>>>() {
            });
            mNameMap = new HashMap<>();
            CharacterParser characterParser = CharacterParser.getInstance();
            for (String temp : countryMap.keySet()) {
                List<String> list = mNameMap.get(temp);
                String c2 = temp.substring(0, 1);
                String c3 = characterParser.convert(c2);
                String c = c3.substring(0, 1);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(temp);
                mNameMap.put(c, list);
            }
        }
    }


    private void initGridView() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }


}
