package com.demo.weather.fragment;

import com.demo.weather.R;
import com.demo.weather.activity.MainActivity;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherDateType;
import com.demo.weather.cusview.BoardView;
import com.demo.weather.cusview.ClockWeatherView;
import com.demo.weather.util.LunarUtil;
import com.demo.weather.util.WeatherUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_LOCATION;
import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_WEATHER;


public class TodayFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2 {


    @InjectView(R.id.tv_location_district)
    TextView tvLocationDistrict;
    @InjectView(R.id.iv_roadlocate)
    ImageView ivRoadlocate;
    @InjectView(R.id.tv_location_street)
    TextView tvLocationStreet;
    @InjectView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @InjectView(R.id.iv_air_quality)
    ImageView ivAirQuality;
    @InjectView(R.id.tv_air_quality_desc)
    TextView tvAirQualityDesc;
    @InjectView(R.id.tv_tv_air_quality_num)
    TextView tvTvAirQualityNum;
    @InjectView(R.id.iv_weather)
    ImageView ivWeather;
    @InjectView(R.id.tv_weather_desc)
    TextView tvWeatherDesc;
    @InjectView(R.id.ll_temperature)
    LinearLayout llTemperature;
    @InjectView(R.id.boardView)
    BoardView boardView;
    @InjectView(R.id.iv_yifu)
    ImageView ivYifu;
    @InjectView(R.id.live_weather_desc)
    TextView liveWeatherDesc;
    @InjectView(R.id.tv_weather_today_desc)
    TextView tvWeatherTodayDesc;
    @InjectView(R.id.tv_clothes_suggest)
    TextView tvClothesSuggest;
    @InjectView(R.id.tv_caleddar)
    TextView tvCaleddar;
    @InjectView(R.id.tv_umbrella)
    TextView tvUmbrella;
    @InjectView(R.id.tv_guomin)
    TextView tvGuomin;
    @InjectView(R.id.tv_illness)
    TextView tvIllness;
    @InjectView(R.id.tv_ultraviolet)
    TextView tvUltraviolet;
    @InjectView(R.id.tv_bask_clothes)
    TextView tvBaskClothes;
    @InjectView(R.id.tv_exercise)
    TextView tvExercise;
    @InjectView(R.id.tv_fish)
    TextView tvFish;
    @InjectView(R.id.ps_scroll)
    PullToRefreshScrollView psScroll;


    @InjectView(R.id.tv_current_temp)
    TextView tvCurrentTemp;
    @InjectView(R.id.tv_current_weather_desc)
    TextView tvCurrentWeatherDesc;
    @InjectView(R.id.tv_wind)
    TextView tvWind;
    @InjectView(R.id.tv_wind_level)
    TextView tvWindLevel;
    @InjectView(R.id.tv_air_pressure_num)
    TextView tvAirPressureNum;
    @InjectView(R.id.tv_humidity)
    TextView tvHumidity;
    @InjectView(R.id.tv_sunup)
    TextView tvSunup;
    @InjectView(R.id.tv_sunset)
    TextView tvSunset;
    @InjectView(R.id.tt_tomorrow_detail)
    TextView ttTomorrowDetail;
    @InjectView(R.id.rl_weatherlist_more)
    RelativeLayout rlWeatherlistMore;
    @InjectView(R.id.hc_scroll)
    HorizontalScrollView hcScroll;
    @InjectView(R.id.ll_today)
    LinearLayout llToday;


    private WeatherBroadcast weatherBroadcast;

    private long updatetime = 0;


    private Weather weather;

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (updatetime == 0) {
                        tvUpdateTime.setText("刚刚更新");
                    }
                    updatetime = System.currentTimeMillis();
                    if (weather != null) {
                        setWeatherData();
                        llToday.removeAllViews();
                        for (int i = 0; i < 24; i++) {
                            ClockWeatherView view = new ClockWeatherView(getContext());
                            view.setData();
                            llToday.addView(view);
                        }
                    }
                    psScroll.onRefreshComplete();
                    break;
            }
        }
    };

    public TodayFragment() {

    }

    private WeatherDateType type;

    public void setWeatherDateType(WeatherDateType type) {
        this.type = type;
    }

    public static TodayFragment newInstance(Weather data, WeatherDateType type) {
        TodayFragment fragment = new TodayFragment();
        fragment.setWeather(data);
        fragment.setWeatherDateType(type);
        return fragment;
    }

    private class WeatherBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UPDATE_LOCATION:
                    tvLocationDistrict.setText(intent.getStringExtra("district"));
                    tvLocationStreet.setText(intent.getStringExtra("street"));
                    break;
                case UPDATE_WEATHER:
                    ArrayList<Weather> datas = intent.getParcelableArrayListExtra("weather");
                    if (datas != null) {
                        switch (type) {
                            case TODAY:
                                weather = datas.get(0) != null ? datas.get(0) : null;
                                break;
                            case TOMORROW:
                                weather = datas.get(1) != null ? datas.get(1) : null;
                                break;
                        }
                        setWeatherData();
                        llToday.removeAllViews();
                        for (int i = 0; i < 24; i++) {
                            ClockWeatherView view = new ClockWeatherView(getContext());
                            view.setData();
                            llToday.addView(view);
                        }

                    }
                    break;
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.inject(this, view);
        locate();
        initScroll();
        if (weather != null) {
            setWeatherData();
            llToday.removeAllViews();
            for (int i = 0; i < 24; i++) {
                ClockWeatherView temp = new ClockWeatherView(getContext());
                temp.setData();
                llToday.addView(temp);
            }
        }
        return view;
    }


    private void locate() {
        if (MainActivity.location != null) {
            tvLocationDistrict.setText(MainActivity.location.getDistrict());
            tvLocationStreet.setText(MainActivity.location.getStreet());
        }
    }

    private void initScroll() {
        psScroll.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    private void setWeatherData() {
        //设置天气描述
        tvWeatherDesc.setText(weather.getWeatherDesc());

        //设置温度图片
        ivWeather.setImageResource(WeatherUtil.getWeatherResId(weather.getWeatherCode()));

        llTemperature.removeAllViews();
        //设置温度范围
        setTem(weather.getTemperatureMin(), 0);
        setTem(weather.getTemperatureMax(), 1);

        //设置空气质量图片
        ivAirQuality.setImageResource(WeatherUtil.getAirPicResId(weather.getAirNum()));
        //设置空气质量级别描述
        tvAirQualityDesc.setText(WeatherUtil.getDes(getContext(), weather.getAirNum(), 1));
        //设置空气质量指数
        tvTvAirQualityNum.setText(String.valueOf(weather.getAirNum()));

        if (boardView.getVisibility() == View.GONE) {
            boardView.setVisibility(View.VISIBLE);
        }
        //空气质量仪表盘设置
        boardView.setData(weather.getAirNum());

        //设置当前温度
        tvCurrentTemp.setText(weather.getCurrentTemp() + "℃");
        //设置当期天气描述
        tvCurrentWeatherDesc.setText(weather.getCurrentWeatherDesc());
        //设置当前风向
        tvWind.setText(weather.getWindDesc());
        //设置当前风力级别
        tvWindLevel.setText(weather.getWindLevel() + getString(R.string.level));
        //设置当前气压
        tvAirPressureNum.setText(weather.getAirNum() + getString(R.string.air_pressure_unit));
        //设置湿度
        tvHumidity.setText(String.valueOf(weather.getHumidity()));
        //设置紫外线
        tvUltraviolet.setText(weather.getUltraviolet());
        //设置日出
        tvSunup.setText(weather.getSunup());
        //设置日落
        tvSunset.setText(weather.getSunset());


        if (weather.getTemperatureMax() < 5) {
            ivYifu.setImageResource(R.mipmap.live_bu_shufu);
            tvClothesSuggest.setText("建议穿棉衣等冬装");
        } else if (weather.getTemperatureMax() < 10) {
            ivYifu.setImageResource(R.mipmap.live_cool);
            tvClothesSuggest.setText("建议穿毛衣、夹克等厚衣服");
        } else if (weather.getTemperatureMax() < 25) {
            ivYifu.setImageResource(R.mipmap.live_shufu);
            tvClothesSuggest.setText("建议穿薄外套");
        } else {
            ivYifu.setImageResource(R.mipmap.live_hot);
            tvClothesSuggest.setText("建议穿T恤、短裤");
        }

        if (weather.getTemperatureMax() - weather.getTemperatureMin() < 10) {
            tvWeatherTodayDesc.setText("今天气温变化平均");
        } else {
            tvWeatherTodayDesc.setText("今天气温变化较大");
        }

        tvCaleddar.setText(LunarUtil.getLunarDate(weather.getDate()));

        if ("a10".equals(weather.getWeatherCode())//
            || "a11".equals(weather.getWeatherCode())//
            || "a12".equals(weather.getWeatherCode())//
            || "a13".equals(weather.getWeatherCode())//
            || "a14".equals(weather.getWeatherCode())//
            || "a16".equals(weather.getWeatherCode())//
            || "a37".equals(weather.getWeatherCode())//
            || "a39".equals(weather.getWeatherCode())//
            || "a40".equals(weather.getWeatherCode())//
            || "a41".equals(weather.getWeatherCode())//
            || "a42".equals(weather.getWeatherCode())//
            || "a60".equals(weather.getWeatherCode())//
            || "a61".equals(weather.getWeatherCode())//
            || "a64".equals(weather.getWeatherCode())
            ) {
            tvUmbrella.setText("需带伞");
        } else {
            tvUmbrella.setText("无需带伞");
        }

        if (weather.getAirNum() <= 150) {
            tvGuomin.setText("不易过敏");
        } else {
            tvGuomin.setText("易过敏");
        }

        if (weather.getTemperatureMax() < 15) {
            tvIllness.setText("较易发感冒");
        } else {
            tvIllness.setText("不易发感冒");
        }

        if ("a32".equals(weather.getWeatherCode()) && weather.getTemperatureMax() > 30) {
            if (weather.getTemperatureMax() < 35) {
                tvBaskClothes.setText("紫外线较强");
            } else if (weather.getTemperatureMax() < 35) {
                tvBaskClothes.setText("紫外线强");
            } else {
                tvBaskClothes.setText("紫外线很强");
            }
        } else {
            tvBaskClothes.setText("紫外线较弱");
        }

        if (("a28".equals(weather.getWeatherCode()) || "a32".equals(weather.getWeatherCode())) && weather.getTemperatureMax() > 10) {
            tvBaskClothes.setText("较宜晾晒");
        } else {
            tvBaskClothes.setText("不宜晾晒");
        }

        if (weather.getTemperatureMin() < 5 || weather.getAirNum() > 150) {
            tvExercise.setText("不宜晨练");
        } else {
            tvExercise.setText("较宜晨练");
        }

        if ("a32".equals(weather.getWeatherCode()) && weather.getTemperatureMax() > 15) {
            tvFish.setText("较适宜钓鱼");
        } else {
            tvFish.setText("不适宜钓鱼");
        }


    }


    private void setTem(int temp, int type) {
        ImageView tempView = null;
        if (type == 1) {
            tempView = new ImageView(getContext());
            tempView.setImageResource(R.mipmap.temp_split);
            llTemperature.addView(tempView);
        }

        if (temp < 0) {
            tempView = new ImageView(getContext());
            tempView.setImageResource(R.mipmap.temp_minus);
            llTemperature.addView(tempView);
        }
        temp = Math.abs(temp);
        if (temp < 10) {
            tempView = new ImageView(getContext());
            tempView.setImageResource(getTempPicResId(temp));
            llTemperature.addView(tempView);
        } else {
            tempView = new ImageView(getContext());
            tempView.setImageResource(getTempPicResId(temp / 10));
            llTemperature.addView(tempView);

            tempView = new ImageView(getContext());
            tempView.setImageResource(getTempPicResId(temp % 10));
            llTemperature.addView(tempView);
        }
        if (type == 1) {
            tempView = new ImageView(getContext());
            tempView.setImageResource(R.mipmap.temp_signal);
            llTemperature.addView(tempView);
        }
    }

    private int getTempPicResId(int code) {
        switch (code) {
            case 0:
                return R.mipmap.temp_0;
            case 1:
                return R.mipmap.temp_1;
            case 2:
                return R.mipmap.temp_2;
            case 3:
                return R.mipmap.temp_3;
            case 4:
                return R.mipmap.temp_4;
            case 5:
                return R.mipmap.temp_5;
            case 6:
                return R.mipmap.temp_6;
            case 7:
                return R.mipmap.temp_7;
            case 8:
                return R.mipmap.temp_8;
            case 9:
                return R.mipmap.temp_9;
        }
        return -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        weatherBroadcast = new WeatherBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_WEATHER);
        filter.addAction(UPDATE_LOCATION);
        getContext().registerReceiver(weatherBroadcast, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(weatherBroadcast);
    }
}
