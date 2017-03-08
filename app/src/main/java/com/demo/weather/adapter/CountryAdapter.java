package com.demo.weather.adapter;

import com.demo.weather.R;
import com.demo.weather.bean.WeatherCity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 选择国家适配器
 */
public class CountryAdapter extends BaseAdapter {

    private List<String> mStringList;
    private List<WeatherCity> mWeatherCityList;
    private LayoutInflater inflater;

    private int type;

    public CountryAdapter(Context context, List<String> list) {
        this.mStringList = list;
        inflater = LayoutInflater.from(context);
        type = 0;
    }

    public CountryAdapter(List<WeatherCity> list, Context context) {
        this.mWeatherCityList = list;
        inflater = LayoutInflater.from(context);
        type = 1;
    }

    @Override
    public int getCount() {
        return type == 0 ? mStringList.size() : mWeatherCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return type == 0 ? mStringList.get(position) : mWeatherCityList.get(position);
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
            convertView = inflater.inflate(R.layout.item_add_city, null);
            myViewHolder.cityTextView = (TextView) convertView.findViewById(R.id.tv_city);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        if (type == 0) {
            myViewHolder.cityTextView.setText(mStringList.get(position));
        } else {
            WeatherCity weatherCity = mWeatherCityList.get(position);
            if (weatherCity != null && !TextUtils.isEmpty(weatherCity.getZhongwen())) {
                myViewHolder.cityTextView.setText(weatherCity.getZhongwen());
            } else {
                myViewHolder.cityTextView.setText("");

            }
        }
        return convertView;
    }

    private class MyViewHolder {
        private TextView cityTextView;
    }

}
