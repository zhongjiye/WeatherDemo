package com.demo.weather.adapter;

import com.demo.weather.R;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.cusview.MyHeaderTitleView;
import com.demo.weather.util.WeatherUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhongjy on 2017/2/23.
 */

public class HeaderTitleAdapter extends BaseAdapter {

    private List<WeatherCity> dasts;
    private LayoutInflater inflater;

    public HeaderTitleAdapter(Context context, List<WeatherCity> list) {
        this.dasts = list;
        inflater = LayoutInflater.from(context);
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

    private int selectItem;

    public void setSelectItem(int selectItem) {
        if (this.selectItem != selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            myViewHolder = new MyViewHolder();
            convertView = inflater.inflate(R.layout.item_header_title, null);
            myViewHolder.headerTitleView = (MyHeaderTitleView) convertView.findViewById(R.id.cus_title);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        WeatherCity weatherCity = dasts.get(position);
        if (weatherCity != null) {
            myViewHolder.headerTitleView.setData(weatherCity);
            if (selectItem == position) {
                myViewHolder.headerTitleView.scaleView(false);
            } else {
                myViewHolder.headerTitleView.scaleView(true);
            }
        }
        return convertView;
    }

    private class MyViewHolder {
        private MyHeaderTitleView headerTitleView;
    }

}
