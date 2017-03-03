package com.demo.weather.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.demo.weather.R;
import com.demo.weather.base.BaseFragmentActivity;
import com.demo.weather.bean.Weather;
import com.demo.weather.fragment.AirFragment;
import com.demo.weather.fragment.SettingFragment;
import com.demo.weather.fragment.TodayFragment;
import com.demo.weather.fragment.WeatherDetailFragment;
import com.demo.weather.fragment.WeatherFragment;
import com.demo.weather.util.AppManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.bean.WeatherDateType.TODAY;
import static com.demo.weather.bean.WeatherDateType.TOMORROW;

/**
 * 主界面
 */
public class MainActivity extends BaseFragmentActivity implements WeatherFragment.WeatherMessage,
    WeatherDetailFragment.UpdateWeather, AMapLocationListener {


    public static final String WEATHER_TAG = "weatherFragment";
    public static final String TODAY_TAG = "todayFragment";
    public static final String TOMORROW_TAG = "tomorrowFragment";
    public static final String AIR_TAG = "airFragment";
    public static final String SETTING_TAG = "settingFragment";

    @InjectView(R.id.ib_weather)
    ImageView ibWeather;
    @InjectView(R.id.ib_today)
    ImageView ibToday;
    @InjectView(R.id.ib_tomorrow)
    ImageView ibTomorrow;
    @InjectView(R.id.ib_aqi)
    ImageView ibAqi;
    @InjectView(R.id.ib_setting)
    ImageView ibSetting;


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

    public static AMapLocation location = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment weatherFragment, todayFragment, tomorrowFragment, airFragment, settingFragment;

    private ArrayList<Weather> weatherList;

    @Override
    public void updateTodayWeather(ArrayList list) {
        this.weatherList = list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        fragmentManager = getSupportFragmentManager();
        setSelectTab(0);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                location = amapLocation;
            } else {
                Log.e("city", amapLocation.getErrorInfo());
            }
        }
    }

    @OnClick({R.id.ib_weather, R.id.ib_today, R.id.ib_tomorrow, R.id.ib_aqi, R.id.ib_setting, R.id.tv_edit_city, R.id
        .tv_add_city, R.id.tv_cancel_update, R.id.tv_confirm_update, R.id.tv_message_board, R.id
        .tv_about_weather, R.id.tv_exit_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_weather:
                setSelectTab(0);
                break;
            case R.id.ib_today:
                setSelectTab(1);
                break;
            case R.id.ib_tomorrow:
                setSelectTab(2);
                break;
            case R.id.ib_aqi:
                setSelectTab(3);
                break;
            case R.id.ib_setting:
                setSelectTab(4);
                break;
            case R.id.tv_edit_city:
                llEditCity.setVisibility(View.GONE);
                llUpdate.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_add_city:
                break;
            case R.id.tv_cancel_update:
                llEditCity.setVisibility(View.VISIBLE);
                llUpdate.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm_update:
                break;
            case R.id.tv_message_board:
                break;
            case R.id.tv_about_weather:
                break;
            case R.id.tv_exit_app://退出应用
                AppManager.getAppManager().AppExit();
                break;
        }
    }

    /**
     * 设置选中的Tab
     */
    public void setSelectTab(int index) {

        ibWeather.setImageResource(R.mipmap.nav_weather_default);
        ibToday.setImageResource(R.mipmap.nav_today_default);
        ibTomorrow.setImageResource(R.mipmap.nav_tomorrow_default);
        ibAqi.setImageResource(R.mipmap.nav_aqi_default);
        ibSetting.setImageResource(R.mipmap.nav_setting_default);

        transaction = fragmentManager.beginTransaction();

        weatherFragment = fragmentManager.findFragmentByTag(WEATHER_TAG);
        todayFragment = fragmentManager.findFragmentByTag(TODAY_TAG);
        tomorrowFragment = fragmentManager.findFragmentByTag(TOMORROW_TAG);
        airFragment = fragmentManager.findFragmentByTag(AIR_TAG);
        settingFragment = fragmentManager.findFragmentByTag(SETTING_TAG);

        if (weatherFragment != null) {
            transaction.hide(weatherFragment);
        }
        if (todayFragment != null) {
            transaction.hide(todayFragment);
        }
        if (tomorrowFragment != null) {
            transaction.hide(tomorrowFragment);
        }
        if (airFragment != null) {
            transaction.hide(airFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }

        switch (index) {
            case 0://天气
                if (weatherFragment == null) {
                    weatherFragment = WeatherFragment.newInstance();
                    transaction.add(R.id.main_fragment, weatherFragment, WEATHER_TAG);
                } else {
                    transaction.show(weatherFragment);
                }
                ibWeather.setImageResource(R.mipmap.nav_weather);
                break;
            case 1://今日天气
                if (todayFragment == null) {
                    todayFragment = TodayFragment.newInstance((weatherList != null && weatherList.size() >= 1) ?
                        weatherList.get(0) : null, TODAY);
                    transaction.add(R.id.main_fragment, todayFragment, TODAY_TAG);
                } else {
                    transaction.show(todayFragment);
                }
                ibToday.setImageResource(R.mipmap.nav_today);
                break;
            case 2://明天天气
                if (tomorrowFragment == null) {
                    tomorrowFragment = TodayFragment.newInstance((weatherList != null && weatherList.size() >= 2) ?
                        weatherList.get(1) : null, TOMORROW);
                    transaction.add(R.id.main_fragment, tomorrowFragment, TOMORROW_TAG);
                } else {
                    transaction.show(tomorrowFragment);
                }
                ibTomorrow.setImageResource(R.mipmap.nav_tomorrow);
                break;
            case 3://空气质量
                if (airFragment == null) {
                    airFragment = AirFragment.newInstance();
                    transaction.add(R.id.main_fragment, airFragment, AIR_TAG);
                } else {
                    transaction.show(airFragment);
                }
                ibAqi.setImageResource(R.mipmap.nav_aqi);
                break;
            case 4://设置
                if (settingFragment == null) {
                    settingFragment = SettingFragment.newInstance();
                    transaction.add(R.id.main_fragment, settingFragment, SETTING_TAG);
                } else {
                    transaction.show(settingFragment);
                }
                ibSetting.setImageResource(R.mipmap.nav_setting);
                break;
        }
        transaction.commit();
    }

    @Override
    public void toggleLeftMenu() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


}
