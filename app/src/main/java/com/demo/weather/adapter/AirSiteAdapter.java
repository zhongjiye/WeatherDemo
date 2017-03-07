package com.demo.weather.adapter;

import com.demo.weather.R;
import com.demo.weather.bean.AirData;
import com.demo.weather.bean.Weather;
import com.demo.weather.bean.WeatherCity;
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

public class AirSiteAdapter extends BaseAdapter {

    private List<AirData> dasts;
    private LayoutInflater inflater;
    private boolean editFlag = false;
    private int itemCount = 3;


    public AirSiteAdapter(Context context, List<AirData> list) {
        this.dasts = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        if (dasts.size() > 3) {
            return itemCount;
        } else {
            return dasts.size();
        }
    }

    @Override
    public AirData getItem(int position) {
        return dasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItemNum(int number) {
        itemCount = number;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            myViewHolder = new MyViewHolder();
            convertView = inflater.inflate(R.layout.item_air_site, null);
            myViewHolder.siteTxt = (TextView) convertView.findViewById(R.id.tv_observe_site);
            myViewHolder.airNumTxt = (TextView) convertView.findViewById(R.id.tv_air_num);
            myViewHolder.pmNumTxt = (TextView) convertView.findViewById(R.id.tv_pm_num);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        AirData airData = dasts.get(position);
        if (airData != null) {
            myViewHolder.siteTxt.setText(airData.getObserveSite());
            myViewHolder.airNumTxt.setText(String.valueOf(airData.getAirNum()));
            myViewHolder.airNumTxt.setBackgroundResource(getBgResId(airData.getAirNum()));
            myViewHolder.pmNumTxt.setText(airData.getPmNum() + "μg/m³");
        }
        return convertView;
    }


    private int getBgResId(int data) {
        if (data >= 0 && data < 25) {
            return R.drawable.air_level1_bg;
        } else if (data >= 25 && data < 75) {
            return R.drawable.air_level2_bg;
        } else if (data >= 75 && data < 125) {
            return R.drawable.air_level3_bg;
        } else if (data >= 125 & data < 175) {
            return R.drawable.air_level4_bg;
        } else if (data >= 175 && data < 250) {
            return R.drawable.air_level5_bg;
        }
        return R.drawable.air_level6_bg;
    }

    private class MyViewHolder {
        private TextView siteTxt, airNumTxt, pmNumTxt;
    }


}
