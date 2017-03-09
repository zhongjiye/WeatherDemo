package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.R;
import com.demo.weather.adapter.CountryAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.CharacterParser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 选择国家
 */
public class SelectCountryActivity extends BaseActivity implements CountryAdapter.OnTagClickListener {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.listview)
    ListView mListview;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;


    private CountryAdapter mCountryAdapter;
    private Map<String, List<String>> mNameMap;
    private Map<String, List<WeatherCity>> mCountryMap;
    private List<String> mIndexList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        ButterKnife.inject(this);
        mTvTitle.setText(R.string.select_country);
        initData();
        initGridView();
    }

    private void initData() {
        String datas = getIntent().getStringExtra("countryMap");
        if (!TextUtils.isEmpty(datas)) {
            mCountryMap = JSON.parseObject(datas, new TypeReference<Map<String, List<WeatherCity>>>() {
            });
            mNameMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            CharacterParser characterParser = CharacterParser.getInstance();
            for (String temp : mCountryMap.keySet()) {
                String c = characterParser.convert(temp.substring(0, 1)).substring(0, 1);
                List<String> list = mNameMap.get(c);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(temp);
                mNameMap.put(c, list);
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
    public void onTagClik(String name) {
        if (!TextUtils.isEmpty(name)) {
            List<WeatherCity> areaList = mCountryMap.get(name);
            if (areaList != null && areaList.size() != 0) {
                Intent intent = new Intent(this, SelectCountryAreaActivity.class);
                intent.putExtra("areaList", JSON.toJSONString(areaList));
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }


}
