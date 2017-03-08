package com.demo.weather.util;

import android.content.Context;

import com.demo.weather.R;

import java.util.HashMap;


/**
 * Created by zhongjy on 2017/2/22.
 */

public class WeatherUtil {
    private static HashMap<String, Integer> weatherMap;

    public static int getWeatherResId(String weatherCode) {
        if (weatherMap == null || weatherMap.size() == 0) {
            initWeatherData();
        }
        return weatherMap.get(weatherCode);
    }


    private static void initWeatherData() {
        weatherMap = new HashMap<>();
        weatherMap.put("a10", R.mipmap.a_10);
        weatherMap.put("a11", R.mipmap.a_11);
        weatherMap.put("a12", R.mipmap.a_12);
        weatherMap.put("a13", R.mipmap.a_13);
        weatherMap.put("a14", R.mipmap.a_14);
        weatherMap.put("a16", R.mipmap.a_16);
        weatherMap.put("a19", R.mipmap.a_19);
        weatherMap.put("a20", R.mipmap.a_20);
        weatherMap.put("a26", R.mipmap.a_26);
        weatherMap.put("a28", R.mipmap.a_28);
        weatherMap.put("a32", R.mipmap.a_32);
        weatherMap.put("a37", R.mipmap.a_37);
        weatherMap.put("a39", R.mipmap.a_39);
        weatherMap.put("a40", R.mipmap.a_40);
        weatherMap.put("a41", R.mipmap.a_41);
        weatherMap.put("a42", R.mipmap.a_42);
        weatherMap.put("a60", R.mipmap.a_60);
        weatherMap.put("a61", R.mipmap.a_61);
        weatherMap.put("a62", R.mipmap.a_62);
        weatherMap.put("a63", R.mipmap.a_63);
        weatherMap.put("a64", R.mipmap.a_64);
        weatherMap.put("a65", R.mipmap.a_65);
        weatherMap.put("b20", R.mipmap.b_20);
        weatherMap.put("b10", R.mipmap.b_28);
        weatherMap.put("b10", R.mipmap.b_32);
        weatherMap.put("b10", R.mipmap.b_39);
        weatherMap.put("b10", R.mipmap.b_41);
        weatherMap.put("b10", R.mipmap.b_65);
    }

    public static int getAirPicResId(int airNum) {
        if (airNum >= 0 && airNum < 25) {
            return R.mipmap.icon_air_1;
        } else if (airNum >= 25 && airNum < 75) {
            return R.mipmap.icon_air_2;
        } else if (airNum >= 75 && airNum < 125) {
            return R.mipmap.icon_air_3;
        } else if (airNum >= 125 & airNum < 175) {
            return R.mipmap.icon_air_4;
        } else if (airNum >= 175 && airNum < 250) {
            return R.mipmap.icon_air_5;
        } else if (airNum >= 250 && airNum < 400) {
            return R.mipmap.icon_air_6;
        } else if (airNum >= 400 && airNum <= 500) {
            return R.mipmap.icon_air_7;
        }
        return 0;
    }

    public static String getDes(Context context, int data, int type) {
        if (data >= 0 && data < 50) {
            return type == 0 ? context.getString(R.string.level_1) : context.getString(R.string
                .level_desc1);
        } else if (data >= 50 && data < 100) {
            return type == 0 ? context.getString(R.string.level_2) : context.getString(R.string
                .level_desc2);
        } else if (data >= 100 && data < 150) {
            return type == 0 ? context.getString(R.string.level_3) : context.getString(R.string
                .level_desc3);
        } else if (data >= 150 & data < 200) {
            return type == 0 ? context.getString(R.string.level_4) : context.getString(R.string
                .level_desc4);
        } else if (data >= 200 && data < 300) {
            return type == 0 ? context.getString(R.string.level_5) : context.getString(R.string
                .level_desc5);
        } else if (data >= 300 && data < 500) {
            return type == 0 ? context.getString(R.string.level_6) : context.getString(R.string
                .level_desc6);
        } else if (data >= 400) {
            return type == 0 ? context.getString(R.string.level_7) : context.getString(R.string
                .level_desc7);
        } else {
            return context.getString(R.string.unknown);
        }
    }
}
