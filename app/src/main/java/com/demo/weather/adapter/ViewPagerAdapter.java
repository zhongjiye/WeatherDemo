package com.demo.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.demo.weather.fragment.WeatherDetailFragment;

import java.util.ArrayList;

/**
 * 天气适配器
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<WeatherDetailFragment> list;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<WeatherDetailFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

}
