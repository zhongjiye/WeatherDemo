package com.demo.weather.adapter;

import com.demo.weather.R;
import com.demo.weather.bean.Advert;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.WeatherUtil;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjy on 2017/2/23.
 */

public class WeatherCityAdapter extends BaseAdapter {

    private List<WeatherCity> dasts;
    private LayoutInflater inflater;
    private DeleteItemListener deleteItemListener;
    private boolean editFlag = false;

    public void setEditFlag(boolean flag) {
        this.editFlag = flag;
    }

    public WeatherCityAdapter(Context context, List<WeatherCity> list, DeleteItemListener
        listener) {
        this.dasts = list;
        inflater = LayoutInflater.from(context);
        deleteItemListener = listener;
    }


    @Override
    public int getCount() {
        return dasts.size();
    }

    @Override
    public WeatherCity getItem(int position) {
        return dasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            myViewHolder = new MyViewHolder();
            convertView = inflater.inflate(R.layout.item_weather_city, null);
            myViewHolder.weahterLayout = (LinearLayout) convertView.findViewById(R.id.ll_weather);
            myViewHolder.weatherRangeLayout = (LinearLayout) convertView.findViewById(R.id
                .ll_weather_range);
            myViewHolder.deleteLayout = (RelativeLayout) convertView.findViewById(R.id.rl_delete_city);
            myViewHolder.weatherImg = (ImageView) convertView.findViewById(R.id.iv_weather);
            myViewHolder.locateImg = (ImageView) convertView.findViewById(R.id.iv_locate);
            myViewHolder.cityTxt = (TextView) convertView.findViewById(R.id.tv_city);
            myViewHolder.defaultCityTxt = (TextView) convertView.findViewById(R.id.tv_city_default);
            myViewHolder.weatherDescTxt = (TextView) convertView.findViewById(R.id.tv_weather_desc);
            myViewHolder.weatherRangeTxt = (TextView) convertView.findViewById(R.id.tv_weather_range);
            myViewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.iv_delete);
            myViewHolder.setDefaultTxt = (TextView) convertView.findViewById(R.id.tv_set_default);
            myViewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItemListener.delete(position);
                }
            });
            myViewHolder.setDefaultTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItemListener.setDefault(position);
                }
            });
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        WeatherCity weatherCity = dasts.get(position);
        if (weatherCity != null) {
            if (weatherCity.isLocate()) {
                myViewHolder.locateImg.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.locateImg.setVisibility(View.INVISIBLE);
            }
            if (weatherCity.isDefault()) {
                myViewHolder.defaultCityTxt.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.defaultCityTxt.setVisibility(View.GONE);
            }
            myViewHolder.cityTxt.setText(TextUtils.isEmpty(weatherCity.getZhongwen()) ? "" : weatherCity
                .getZhongwen());
            Weather weather = weatherCity.getWeather();
            if (weather != null) {
                myViewHolder.weatherDescTxt.setText(TextUtils.isEmpty(weather.getWeatherDesc())
                    ? "" : weather.getWeatherDesc());
                myViewHolder.weatherImg.setImageResource(WeatherUtil.getWeatherResId(weather.getWeatherCode()));
                myViewHolder.weatherRangeTxt.setText(weather.getTemperatureMin() + "~" + weather.getTemperatureMax() + "℃");
            }
            if (editFlag) {
                myViewHolder.deleteLayout.setVisibility(View.VISIBLE);
                if (weatherCity.isDefault()) {
                    myViewHolder.setDefaultTxt.setVisibility(View.GONE);
                } else {
                    myViewHolder.setDefaultTxt.setVisibility(View.VISIBLE);
                }
                myViewHolder.weahterLayout.setVisibility(View.GONE);
                myViewHolder.weatherRangeLayout.setVisibility(View.GONE);
            } else {
                myViewHolder.deleteLayout.setVisibility(View.GONE);
                myViewHolder.weahterLayout.setVisibility(View.VISIBLE);
                myViewHolder.weatherRangeLayout.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    private class MyViewHolder {
        private TextView cityTxt, defaultCityTxt, weatherDescTxt, weatherRangeTxt, setDefaultTxt;
        private ImageView locateImg, weatherImg, deleteImg;
        private LinearLayout weahterLayout, weatherRangeLayout;
        private RelativeLayout deleteLayout;
    }

    public interface DeleteItemListener {
        void delete(int position);

        void setDefault(int position);
    }


}
