package com.demo.weather.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.weather.bean.WeatherCity;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhongjy on 2017/3/8.
 */

public class CityDataUtil {
    private static Map<String, Map<String, List<WeatherCity>>> mCityMap;
    private static Map<String, Map<String, List<WeatherCity>>> mForeignCityMap;

    public static Map<String, Map<String, List<WeatherCity>>> getCityMap(Context context) {
        if (mCityMap == null || mCityMap.size() == 0) {
            String datas = AssetsUtil.getAssetFile(context, "citys.json");
            if (!TextUtils.isEmpty(datas)) {
                mCityMap = JSON.parseObject(datas,
                    new TypeReference<TreeMap<String, Map<String, List<WeatherCity>>>>() {
                    });
            }
        }
        return mCityMap;
    }

    public static Map<String, Map<String, List<WeatherCity>>> getForeignCityMap(Context context) {
        if (mForeignCityMap == null || mForeignCityMap.size() == 0) {
            String datas = AssetsUtil.getAssetFile(context, "internal_citys.json");
            if (!TextUtils.isEmpty(datas)) {
                mForeignCityMap = JSON.parseObject(datas,
                    new TypeReference<Map<String, Map<String, List<WeatherCity>>>>() {
                    });
            }
        }
        return mForeignCityMap;
    }
}
