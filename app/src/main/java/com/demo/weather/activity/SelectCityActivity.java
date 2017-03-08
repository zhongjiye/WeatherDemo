package com.demo.weather.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.R;
import com.demo.weather.adapter.CityAdapter;
import com.demo.weather.base.BaseActivity;
import com.demo.weather.bean.WeatherCity;

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
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加城市
 */
public class SelectCityActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_province)
    TextView mTvProvince;
    @InjectView(R.id.gv_city)
    GridView mGvCity;

    private String mProvince;
    private CityAdapter mCityAdapter;
    private List<String> mCityList;
    private Map<String, List<WeatherCity>> cityMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        ButterKnife.inject(this);
        initData();
        initGridView();
    }

    private void initData() {
        String datas = getIntent().getStringExtra("cityMap");
        mProvince = getIntent().getStringExtra("province");
        if (!TextUtils.isEmpty(datas)) {
            cityMap = JSON.parseObject(datas, new TypeReference<Map<String, List<WeatherCity>>>() {
            });
            mCityList = new ArrayList<>();
            for (String temp : cityMap.keySet()) {
                mCityList.add(temp);
            }
        }
    }


    private void initGridView() {
        mTvProvince.setText(TextUtils.isEmpty(mProvince) ? "" : mProvince);
        mCityAdapter = new CityAdapter(this, mCityList);
        mGvCity.setAdapter(mCityAdapter);
        mGvCity.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String city = mCityList.get(position);
        if (!TextUtils.isEmpty(city)) {
            List<WeatherCity> areaList = cityMap.get(city);
            if (areaList != null && areaList.size() != 0) {
                Intent intent = new Intent(this, SelectAreaActivity.class);
                intent.putExtra("areaList", JSON.toJSONString(areaList));
                intent.putExtra("area", city);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }


}
