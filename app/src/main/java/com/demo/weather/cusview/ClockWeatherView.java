package com.demo.weather.cusview;

import android.content.Context;
import android.graphics.LinearGradient;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.weather.R;
import com.demo.weather.util.WeatherUtil;

/**
 * Created by zhongjy on 2017/2/24.
 */

public class ClockWeatherView extends LinearLayout {

    private TextView clockView, weatherDescView, temperatureView, airLevelView;
    private ImageView weatherPicView;


    public ClockWeatherView(Context context) {
        super(context);
        init(context);
    }

    public ClockWeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClockWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.item_today_weather, this);
        clockView = (TextView) view.findViewById(R.id.tv_clock);
        weatherDescView = (TextView) view.findViewById(R.id.tv_weather_desc);
        temperatureView = (TextView) view.findViewById(R.id.tv_temperature);
        airLevelView = (TextView) view.findViewById(R.id.tv_air_level);
        weatherPicView = (ImageView) view.findViewById(R.id.iv_weather);
    }

    public void setData() {
        clockView.setText("0点");
        weatherDescView.setText("多云");
        temperatureView.setText("5℃");
        airLevelView.setText("优");
        weatherPicView.setImageResource(WeatherUtil.getWeatherResId("a32"));
    }
}
