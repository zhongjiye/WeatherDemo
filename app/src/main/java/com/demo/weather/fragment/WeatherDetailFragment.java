package com.demo.weather.fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.demo.weather.R;
import com.demo.weather.activity.MainActivity;
import com.demo.weather.adapter.WeatherAdapter;
import com.demo.weather.bean.Advert;
import com.demo.weather.bean.Weather;
import com.demo.weather.util.DateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WeatherDetailFragment extends Fragment implements PullToRefreshBase
    .OnRefreshListener2, View.OnClickListener, AdapterView.OnItemClickListener, AMapLocationListener {


    public interface WeatherDetailMessage {
        void setDistrict(String text);
    }

    public interface UpdateWeather {
        void updateTodayWeather(ArrayList<Weather> list);
    }

    @InjectView(R.id.ps_listview)
    PullToRefreshListView mPsListview;
    private View rootView;


    private Weather[] weatherDatas = new Weather[5];
    private Advert[] advertDatas = new Advert[5];

    private WeatherAdapter weatherAdapter;
    private ArrayList<Weather> weatherList;
    private ArrayList<Advert> adList;
    private Weather weather;


    private WeatherDetailMessage weatherDetailMessage;
    private UpdateWeather updateWeather;

    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;

    private String location;
    private boolean isDefault;


    private void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWeatherDetailMessage(WeatherDetailMessage message) {
        this.weatherDetailMessage = message;
    }

    public boolean getDefault() {
        return isDefault;
    }

    public String getLocation() {
        return location;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mPsListview.setRefreshing();
                    break;
                case 1:
                    getWeatherListData();
                    getWeatherData();
                    setAdvert(0);
                    weatherAdapter.notifyDataSetChanged();
                    mPsListview.onRefreshComplete();
                    break;
                case 2:
                    setAdvert(1);
                    weatherAdapter.notifyDataSetChanged();
                    mPsListview.onRefreshComplete();
                    break;
            }
        }
    };

    public WeatherDetailFragment() {

    }

    public static WeatherDetailFragment newInstance(WeatherDetailMessage message, String location, boolean isDefault) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        fragment.setLocation(location);
        fragment.setDefault(isDefault);
        fragment.setWeatherDetailMessage(message);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_weather_detail, container, false);
            ButterKnife.inject(this, rootView);
            initDatas();
            initListView();
            locate();
            handler.sendEmptyMessageDelayed(0, 500);
            mPsListview.setShowIndicator(false);
        }
        ButterKnife.inject(this, rootView);
        return rootView;
    }


    private void initDatas() {
        weatherDatas[0] = new Weather("多云转晴", "a20", 5, 18, 6, "多云", 2, "西北风", 1025, 141, "2017-02-23", 25, "05:35",
            "17:32", "弱");
        weatherDatas[1] = new Weather("多云", "a28", 8, 22, 6, "多云", 4, "西南风", 925, 121, "2017-02-23", 25, "06:35",
            "17:46", "弱");
        weatherDatas[2] = new Weather("晴", "a32", 16, 30, 6, "晴", 1, "北风", 1047, 40, "2017-02-23", 25, "06:05",
            "17:56", "强");
        weatherDatas[3] = new Weather("大雨", "a12", 5, 9, 6, "大雨", 3, "西风", 1045, 5, "2017-02-23", 25, "05:46",
            "17:49", "弱");
        weatherDatas[4] = new Weather("小雪", "a13", -5, 4, 6, "小雪", 1, "东风", 1025, 200, "2017-02-23", 25, "06:35",
            "18:32", "较弱");

        advertDatas[0] = new Advert("这是广告啦1", Arrays.asList("http://img2.3lian.com/2014/f2/37/d/40.jpg"), 0);
        advertDatas[1] = new Advert("这是广告啦2", Arrays.asList("http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://img2" + ".3lian.com/2014/f2/37/d/39.jpg", "http://www.8kmm" + "" +
                ".com/UploadFiles/2012/8/201208140920132659.jpg"), 1);
        advertDatas[2] = new Advert("这是广告啦3", Arrays.asList("http://www.8kmm" + "" +
            ".com/UploadFiles/2012/8/201208140920132659.jpg", "http://f.hiphotos.baidu" +
            ".com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779" +
            ".jpg", "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"), 1);
        advertDatas[3] = new Advert("这是广告啦4", Arrays.asList("http://img2.3lian.com/2014/f2/37/d/40.jpg"), 0);
        advertDatas[4] = new Advert("这是广告啦5", Arrays.asList("http://f.hiphotos.baidu" + "" +
            ".com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"), 1);
    }


    private void initListView() {
        weatherList = new ArrayList<>();
        adList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getContext(), weatherList, adList, this);
        mPsListview.setAdapter(weatherAdapter);
        mPsListview.setOnRefreshListener(this);
        mPsListview.setMode(PullToRefreshBase.Mode.BOTH);
        mPsListview.setOnItemClickListener(this);
    }

    private void locate() {
        mlocationClient = new AMapLocationClient(getContext());
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private void getWeatherListData() {
        weatherList.clear();
        for (int i = 0; i < 15; i++) {
            try {
                Weather temp = (Weather) (weatherDatas[(new Random()).nextInt(4) % 5]).clone();
                String time = DateUtil.getSomeDay(new Date(), i);
                temp.setDate(time);
                weatherList.add(i, temp);
            } catch (Exception e) {

            }
        }
    }

    private void setAdvert(int type) {
        switch (type) {
            case 0:
                adList.clear();
                for (int i = 0; i < 5; i++) {
                    try {
                        Advert temp = (Advert) (advertDatas[(new Random()).nextInt(4) % 5]).clone();
                        temp.setContent("这是第" + i + "条广告");
                        adList.add(temp);
                    } catch (Exception e) {

                    }
                }
                break;
            case 1:
                int count = adList.size();
                for (int i = 0; i < 5; i++) {
                    try {
                        Advert temp = (Advert) (advertDatas[(new Random()).nextInt(4) % 5]).clone();
                        temp.setContent("这是第" + (i + count) + "条广告");
                        adList.add(temp);
                    } catch (Exception e) {

                    }
                }
                break;
        }
    }

    private void getWeatherData() {
        weather = weatherList.get(0);
        Intent intent = new Intent();
        intent.setAction(TodayFragment.UPDATE_WEATHER);
        intent.putParcelableArrayListExtra("weather", weatherList);
        getContext().sendBroadcast(intent);
        if (updateWeather != null) {
            updateWeather.updateTodayWeather(weatherList);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (weatherDetailMessage != null) {
                    weatherDetailMessage.setDistrict(aMapLocation.getDistrict());
                }
//                tvLocationStreet.setText(aMapLocation.getStreet());
                ((AMapLocationListener) getContext()).onLocationChanged(aMapLocation);
                Intent intent = new Intent();
                intent.setAction(TodayFragment.UPDATE_LOCATION);
                intent.putExtra("district", aMapLocation.getDistrict());
                intent.putExtra("street", aMapLocation.getStreet());
                getContext().sendBroadcast(intent);
            } else {
                Log.e("city", aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        handler.sendEmptyMessageDelayed(1, 1500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        handler.sendEmptyMessageDelayed(2, 1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_today_air:

                break;
            case R.id.tv_month_air:

                break;
            case R.id.tv_today_detail:
                ((MainActivity) getContext()).setSelectTab(1);
                break;
            case R.id.tv_tomorrow_detail:
                ((MainActivity) getContext()).setSelectTab(2);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 2) {
            ((MainActivity) getContext()).setSelectTab(1);
        } else if (position == 3) {
            ((MainActivity) getContext()).setSelectTab(2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getContext() instanceof UpdateWeather) {
            updateWeather = (UpdateWeather) getContext();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }


}
