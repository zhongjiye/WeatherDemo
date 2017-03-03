package com.demo.weather.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.weather.R;
import com.demo.weather.activity.MainActivity;
import com.demo.weather.adapter.ViewPagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 天气主界面
 */
public class WeatherFragment extends Fragment implements WeatherDetailFragment
    .WeatherDetailMessage, ViewPager
    .OnPageChangeListener {


    public interface WeatherMessage {
        /**
         * 切换侧滑菜单开闭状态
         */
        void toggleLeftMenu();
    }

    @InjectView(R.id.iv_center_locate)
    ImageView ivCenterLocate;
    @InjectView(R.id.tv_center_location_district)
    TextView tvCenterLocationDistrict;
    @InjectView(R.id.ll_center)
    LinearLayout llCenter;
    @InjectView(R.id.iv_left_locate)
    ImageView ivLeftLocate;
    @InjectView(R.id.tv_left_location_district)
    TextView tvLeftLocationDistrict;
    @InjectView(R.id.ll_left)
    LinearLayout llLeft;
    @InjectView(R.id.iv_right_locate)
    ImageView ivRightLocate;
    @InjectView(R.id.tv_right_location_district)
    TextView tvRightLocationDistrict;
    @InjectView(R.id.ll_right)
    LinearLayout llRight;
    @InjectView(R.id.tv_edit_city)
    TextView tvEditCity;
    @InjectView(R.id.tv_add_city)
    TextView tvAddCity;
    @InjectView(R.id.ll_edit_city)
    LinearLayout llEditCity;
    @InjectView(R.id.tv_cancel_update)
    TextView tvCancelUpdate;
    @InjectView(R.id.tv_confirm_update)
    TextView tvConfirmUpdate;
    @InjectView(R.id.ll_update)
    LinearLayout llUpdate;
    @InjectView(R.id.tv_message_board)
    TextView tvMessageBoard;
    @InjectView(R.id.tv_about_weather)
    TextView tvAboutWeather;
    @InjectView(R.id.tv_exit_app)
    TextView tvExitApp;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.vp_main)
    ViewPager vpMain;
    @InjectView(R.id.iv_toggle)
    ImageView ivToggle;
    @InjectView(R.id.iv_add_city)
    ImageView ivAddCity;
    @InjectView(R.id.iv_search_city)
    ImageView ivSearchCity;
    @InjectView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    private int index;
    private ArrayList<WeatherDetailFragment> fragmentList;


    private WeatherDetailFragment weather1, weather2, weather3, weather4;
    private ViewPagerAdapter adapter;

    private WeatherMessage weatherMessage;

    private int lastIndex = -1;

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
        weather1 = WeatherDetailFragment.newInstance(this, "浦东", true);
        weather2 = WeatherDetailFragment.newInstance(this, "南京", false);
        weather3 = WeatherDetailFragment.newInstance(this, "北京", false);
        weather4 = WeatherDetailFragment.newInstance(this, "合肥", false);
        fragmentList = new ArrayList<>();
        fragmentList.add(weather1);
        fragmentList.add(weather2);
        fragmentList.add(weather3);
        fragmentList.add(weather4);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        vpMain.setAdapter(adapter);

        lastIndex = 0;
        if (fragmentList.size() == 1) {
            setData(1, 0);
            llCenter.setVisibility(View.VISIBLE);
            llLeft.setVisibility(View.INVISIBLE);
            llRight.setVisibility(View.INVISIBLE);
        } else if (fragmentList.size() > 1) {
            setData(1, 0);
            setData(2, 1);
            llLeft.setVisibility(View.INVISIBLE);
            llCenter.setVisibility(View.VISIBLE);
            llRight.setVisibility(View.VISIBLE);
        }

        vpMain.addOnPageChangeListener(this);
        vpMain.setOffscreenPageLimit(2);

        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setCircleCount(fragmentList.size());
        circleNavigator.setCircleColor(Color.WHITE);
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, vpMain);


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
                llLeft.setVisibility(View.VISIBLE);
                llRight.setVisibility(View.GONE);
            } else {
                setData(1, 0);
                setData(2, 1);
                llRight.setVisibility(View.VISIBLE);
                llLeft.setVisibility(View.GONE);
            }
        } else if (fragmentList.size() > 2) {
            if (index - lastIndex > 0) {
                if (index != fragmentList.size() - 1) {
                    llLeft.setVisibility(View.VISIBLE);
                    llRight.setVisibility(View.VISIBLE);
                    llCenter.setVisibility(View.VISIBLE);
                    setData(0, index - 1);
                    setData(1, index);
                    setData(2, index + 1);
                } else {
                    llLeft.setVisibility(View.VISIBLE);
                    llRight.setVisibility(View.INVISIBLE);
                    llCenter.setVisibility(View.VISIBLE);
                    setData(0, index - 1);
                    setData(1, index);
                }
            } else {
                if (index != 0) {
                    llLeft.setVisibility(View.VISIBLE);
                    llRight.setVisibility(View.VISIBLE);
                    llCenter.setVisibility(View.VISIBLE);
                    setData(0, index - 1);
                    setData(1, index);
                    setData(2, index + 1);
                } else {
                    llLeft.setVisibility(View.INVISIBLE);
                    llRight.setVisibility(View.VISIBLE);
                    llCenter.setVisibility(View.VISIBLE);
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
                tvLeftLocationDistrict.setText(fragmentList.get(index).getLocation());
                if (fragmentList.get(index).getDefault()) {
                    ivLeftLocate.setVisibility(View.VISIBLE);
                } else {
                    ivLeftLocate.setVisibility(View.GONE);
                }
                break;
            case 1:
                tvCenterLocationDistrict.setText(fragmentList.get(index).getLocation());
                if (fragmentList.get(index).getDefault()) {
                    ivCenterLocate.setVisibility(View.VISIBLE);
                } else {
                    ivCenterLocate.setVisibility(View.GONE);
                }
                break;
            case 2:
                tvRightLocationDistrict.setText(fragmentList.get(index).getLocation());
                if (fragmentList.get(index).getDefault()) {
                    ivRightLocate.setVisibility(View.VISIBLE);
                } else {
                    ivRightLocate.setVisibility(View.GONE);
                }
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getContext() instanceof WeatherMessage) {
            this.weatherMessage = (WeatherMessage) getContext();
        }
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
                break;
            case R.id.iv_search_city:
                break;
        }
    }


}
