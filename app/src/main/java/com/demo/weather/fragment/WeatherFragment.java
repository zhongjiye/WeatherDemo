package com.demo.weather.fragment;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.activity.AddCityActivity;
import com.demo.weather.activity.MainActivity;
import com.demo.weather.adapter.ViewPagerAdapter;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.SharedPreferencesUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.activity.LaunchActivity.WEATHER_CITY_LIST_TAG;
import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_CITYLIST;

/**
 * 天气主界面
 */
public class WeatherFragment extends Fragment implements WeatherDetailFragment
    .WeatherDetailMessage, ViewPager
    .OnPageChangeListener {

    @InjectView(R.id.vp_main)
    ViewPager mVpMain;
    @InjectView(R.id.iv_toggle)
    ImageView mIvToggle;
    @InjectView(R.id.iv_add_city)
    ImageView mIvAddCity;
    @InjectView(R.id.iv_search_city)
    ImageView mIvSearchCity;
    @InjectView(R.id.iv_center_locate)
    ImageView mIvCenterLocate;
    @InjectView(R.id.tv_center_location_district)
    TextView mTvCenterLocationDistrict;
    @InjectView(R.id.ll_center)
    LinearLayout mLlCenter;
    @InjectView(R.id.iv_left_locate)
    ImageView mIvLeftLocate;
    @InjectView(R.id.tv_left_location_district)
    TextView mTvLeftLocationDistrict;
    @InjectView(R.id.ll_left)
    LinearLayout mLlLeft;
    @InjectView(R.id.iv_right_locate)
    ImageView mIvRightLocate;
    @InjectView(R.id.tv_right_location_district)
    TextView mTvRightLocationDistrict;
    @InjectView(R.id.ll_right)
    LinearLayout mLlRight;
    @InjectView(R.id.ll_point)
    LinearLayout mLlPoint;
    @InjectView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    public interface WeatherMessage {
        /**
         * 切换侧滑菜单开闭状态
         */
        void toggleLeftMenu();
    }


    private int index;
    private ArrayList<WeatherDetailFragment> fragmentList;

    private ViewPagerAdapter adapter;

    private WeatherMessage weatherMessage;
    private List<WeatherCity> weatherCityList;

    private int lastIndex = -1;

    private MyReceiver myReceiver;

    public WeatherFragment() {

    }

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.inject(this, view);
        init();
        return view;
    }


    private void init() {
        myReceiver = new MyReceiver();
        getContext().registerReceiver(myReceiver, new IntentFilter(UPDATE_CITYLIST));
        weatherCityList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        String content = String.valueOf(SharedPreferencesUtils.get(getContext(),
            WEATHER_CITY_LIST_TAG, ""));
        if (!TextUtils.isEmpty(content)) {
            List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
            if (list != null && list.size() != 0) {
                weatherCityList.addAll(list);
            }
        }
        for (WeatherCity temp : weatherCityList) {
            fragmentList.add(WeatherDetailFragment.newInstance(this, temp.getCity(), temp.isLocate()));
        }

        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mVpMain.setAdapter(adapter);
        mVpMain.addOnPageChangeListener(this);
        mVpMain.setOffscreenPageLimit(2);

        initGuide();

    }

    private void initGuide() {
        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setCircleCount(fragmentList.size());
        circleNavigator.setCircleColor(Color.WHITE);
        mMagicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mVpMain);

        adapter.notifyDataSetChanged();

        lastIndex = 0;
        if (fragmentList.size() == 1) {
            setData(1, 0);
            mLlCenter.setVisibility(View.VISIBLE);
            mLlLeft.setVisibility(View.INVISIBLE);
            mLlRight.setVisibility(View.INVISIBLE);
        } else if (fragmentList.size() > 1) {
            setData(1, 0);
            setData(2, 1);
            mLlLeft.setVisibility(View.INVISIBLE);
            mLlCenter.setVisibility(View.VISIBLE);
            mLlRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        if (position < 0 || position > fragmentList.size() - 1 || index == position) {
            return;
        }
        index = position;
        if (fragmentList.size() == 2) {
            if (index - lastIndex > 0) {
                setData(0, 0);
                setData(1, 1);
                mLlLeft.setVisibility(View.VISIBLE);
                mLlRight.setVisibility(View.GONE);
            } else {
                setData(1, 0);
                setData(2, 1);
                mLlRight.setVisibility(View.VISIBLE);
                mLlLeft.setVisibility(View.GONE);
            }
        } else if (fragmentList.size() > 2) {
            if (index - lastIndex > 0) {
                if (index != fragmentList.size() - 1) {
                    mLlLeft.setVisibility(View.VISIBLE);
                    mLlRight.setVisibility(View.VISIBLE);
                    mLlCenter.setVisibility(View.VISIBLE);
                    setData(0, index - 1);
                    setData(1, index);
                    setData(2, index + 1);
                } else {
                    mLlLeft.setVisibility(View.VISIBLE);
                    mLlRight.setVisibility(View.INVISIBLE);
                    mLlCenter.setVisibility(View.VISIBLE);
                    setData(0, index - 1);
                    setData(1, index);
                }
            } else {
                if (index != 0) {
                    mLlLeft.setVisibility(View.VISIBLE);
                    mLlRight.setVisibility(View.VISIBLE);
                    mLlCenter.setVisibility(View.VISIBLE);
                    setData(0, index - 1);
                    setData(1, index);
                    setData(2, index + 1);
                } else {
                    mLlLeft.setVisibility(View.INVISIBLE);
                    mLlRight.setVisibility(View.VISIBLE);
                    mLlCenter.setVisibility(View.VISIBLE);
                    setData(1, index);
                    setData(2, index + 1);
                }
            }
        }
        lastIndex = index;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setData(int tab, int index) {
        switch (tab) {
            case 0:
                mTvLeftLocationDistrict.setText(fragmentList.get(index).getLocation());
                if (fragmentList.get(index).getDefault()) {
                    mIvLeftLocate.setVisibility(View.VISIBLE);
                } else {
                    mIvLeftLocate.setVisibility(View.GONE);
                }
                break;
            case 1:
                mTvCenterLocationDistrict.setText(fragmentList.get(index).getLocation());
                if (fragmentList.get(index).getDefault()) {
                    mIvCenterLocate.setVisibility(View.VISIBLE);
                } else {
                    mIvCenterLocate.setVisibility(View.GONE);
                }
                break;
            case 2:
                mTvRightLocationDistrict.setText(fragmentList.get(index).getLocation());
                if (fragmentList.get(index).getDefault()) {
                    mIvRightLocate.setVisibility(View.VISIBLE);
                } else {
                    mIvRightLocate.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void selectPage(int index) {
        mVpMain.setCurrentItem(index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getContext() instanceof WeatherMessage) {
            this.weatherMessage = (WeatherMessage) getContext();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(myReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void setDistrict(String text) {
//        tvLocationDistrict.setText(text);
    }

    @OnClick({R.id.iv_toggle, R.id.iv_add_city, R.id.iv_search_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tomorrow_detail:
                ((MainActivity) getContext()).setSelectTab(2);
                break;
            case R.id.tv_today_detail:
                ((MainActivity) getContext()).setSelectTab(1);
                break;
            case R.id.iv_toggle:
                if (weatherMessage != null) {
                    weatherMessage.toggleLeftMenu();
                }
                break;
            case R.id.iv_add_city:
                startActivity(new Intent(getContext(), AddCityActivity.class));
                break;
            case R.id.iv_search_city:
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UPDATE_CITYLIST:
                    String content = String.valueOf(SharedPreferencesUtils.get(getContext(),
                        WEATHER_CITY_LIST_TAG, ""));
                    if (!TextUtils.isEmpty(content)) {
                        List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
                        if (list != null && list.size() != 0) {
                            weatherCityList.clear();
                            weatherCityList.addAll(list);
                            fragmentList.clear();
                            for (WeatherCity temp : weatherCityList) {
                                fragmentList.add(WeatherDetailFragment.newInstance
                                    (WeatherFragment.this, temp
                                        .getCity(), temp.isLocate()));
                            }
                            adapter.notifyDataSetChanged();
                            initGuide();
                        }
                    }
                    break;
            }
        }
    }
}