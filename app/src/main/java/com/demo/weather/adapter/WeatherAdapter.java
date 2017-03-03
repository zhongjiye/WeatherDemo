package com.demo.weather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.weather.R;
import com.demo.weather.bean.Advert;
import com.demo.weather.bean.Weather;
import com.demo.weather.cusview.BoardView;
import com.demo.weather.util.DateUtil;
import com.demo.weather.util.LunarUtil;
import com.demo.weather.util.WeatherUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import junit.framework.TestResult;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by zhongjy on 2017/2/23.
 */

public class WeatherAdapter extends BaseAdapter {

    private ArrayList<Weather> weathers;
    private ArrayList<Advert> adverts;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private Context context;
    private View.OnClickListener clickListener;

    public WeatherAdapter(Context context, ArrayList<Weather> weathers, ArrayList<Advert>
        adverts, View.OnClickListener clickListener) {
        this.context = context;
        this.weathers = weathers;
        this.adverts = adverts;
        this.clickListener=clickListener;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
            .showStubImage(R.mipmap.icon)          // image在加载过程中，显示的图片
            .showImageForEmptyUri(R.mipmap.icon)  // empty URI时显示的图片
            .showImageOnFail(R.mipmap.icon)       // 不是图片文件 显示图片
            .resetViewBeforeLoading(false)  // default
            .delayBeforeLoading(1000)
            .cacheInMemory(true)           // default 不缓存至内存
            .cacheOnDisc(true)             // default 不缓存至手机SDCard
            .build();
    }


    @Override
    public int getCount() {
        if (weathers.size() == 0 && adverts.size() == 0) {
            return 0;
        }
        return 1 + weathers.size() + 1 + 1 + adverts.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < weathers.size() + 3) {
            return weathers.get(position);
        } else {
            return adverts.get(position - weathers.size() - 3);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static final int SINGLE_ADVERT_TYPE = 0;
    public static final int MULTI_ADVERT_TYPE = 1;
    public static final int WEATHER_HAEADER_TYPE = 2;
    public static final int WEATHER_LIST_ITEM_TYPE = 3;
    public static final int WEATHER_AIR_BOARD_TYPE = 4;
    public static final int WEATHER_LIVE_SUGGEST_TYPE = 5;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return WEATHER_HAEADER_TYPE;
        } else if (position > 0 && position < weathers.size() + 1) {
            return WEATHER_LIST_ITEM_TYPE;
        } else if (position == weathers.size() + 1) {
            return WEATHER_AIR_BOARD_TYPE;
        } else if (position == weathers.size() + 2) {
            return WEATHER_LIVE_SUGGEST_TYPE;
        } else if (position > weathers.size() + 2) {
            return adverts.get(position - weathers.size() - 3).getType();
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherItemHolder weatherItemHolder = null;
        WeatherHeaderHolder weatherHeaderHolder = null;
        AirBoradHolder airBoradHolder = null;
        LiveSuggestHolder liveSuggestHolder = null;
        SingleAdvertViewHolder singleAdvertViewHolder = null;
        MultiAdvertViewHolder multiAdvertViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case SINGLE_ADVERT_TYPE:
                    singleAdvertViewHolder = new SingleAdvertViewHolder();
                    convertView = inflater.inflate(R.layout.item_single_advert, null);
                    singleAdvertViewHolder.advertImg = (ImageView) convertView.findViewById(R.id.iv_advert_img);
                    singleAdvertViewHolder.advertContent = (TextView) convertView.findViewById(R.id.tv_advert_content);
                    convertView.setTag(singleAdvertViewHolder);
                    break;
                case MULTI_ADVERT_TYPE:
                    multiAdvertViewHolder = new MultiAdvertViewHolder();
                    convertView = inflater.inflate(R.layout.item_multittem_advert, null);
                    multiAdvertViewHolder.advertImg1 = (ImageView) convertView.findViewById(R.id.iv_advert_img1);
                    multiAdvertViewHolder.advertImg2 = (ImageView) convertView.findViewById(R.id.iv_advert_img2);
                    multiAdvertViewHolder.advertImg3 = (ImageView) convertView.findViewById(R.id.iv_advert_img3);
                    multiAdvertViewHolder.advertContent = (TextView) convertView.findViewById(R.id.tv_advert_content);
                    convertView.setTag(multiAdvertViewHolder);
                    break;
                case WEATHER_LIST_ITEM_TYPE:
                    weatherItemHolder = new WeatherItemHolder();
                    convertView = inflater.inflate(R.layout.item_weather, null);
                    weatherItemHolder.weatherImg = (ImageView) convertView.findViewById(R.id.iv_weather);
                    weatherItemHolder.arrowImg = (ImageView) convertView.findViewById(R.id.iv_arrow);
                    weatherItemHolder.dateDesc = (TextView) convertView.findViewById(R.id.tv_date_desc);
                    weatherItemHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
                    weatherItemHolder.weather = (TextView) convertView.findViewById(R.id.tv_weather_desc);
                    weatherItemHolder.tempRange = (TextView) convertView.findViewById(R.id.tv_weather_range);
                    convertView.setTag(weatherItemHolder);
                    break;
                case WEATHER_HAEADER_TYPE:
                    weatherHeaderHolder = new WeatherHeaderHolder();
                    convertView = inflater.inflate(R.layout.item_weatherdetail_header, null);
                    weatherHeaderHolder.airPressureNumTxt = (TextView) convertView.findViewById(R.id
                        .tv_air_pressure_num);
                    weatherHeaderHolder.airQualityDescTxt = (TextView) convertView.findViewById(R.id
                        .tv_air_quality_desc);
                    weatherHeaderHolder.airQualityImg = (ImageView) convertView.findViewById(R.id
                        .iv_air_quality);
                    weatherHeaderHolder.currentTempTxt = (TextView) convertView.findViewById(R.id
                        .tv_current_temp);
                    weatherHeaderHolder.currentWeatherDescTxt = (TextView) convertView.findViewById
                        (R.id.tv_current_weather_desc);
                    weatherHeaderHolder.temperatureContainerLayout = (LinearLayout) convertView
                        .findViewById(R.id.ll_temperature);
                    weatherHeaderHolder.todayWeatherDetailTxt = (TextView) convertView.findViewById
                        (R.id.tv_today_detail);
                    weatherHeaderHolder.tomorrowWeatherDetailTxt = (TextView) convertView
                        .findViewById(R.id.tv_tomorrow_detail);
                    weatherHeaderHolder.weaherImg = (ImageView) convertView.findViewById(R.id
                        .iv_weather);
                    weatherHeaderHolder.weatherDescTxt = (TextView) convertView.findViewById(R.id
                        .tv_weather_desc);
                    weatherHeaderHolder.windLevelTxt = (TextView) convertView.findViewById(R.id
                        .tv_wind_level);
                    weatherHeaderHolder.windTxt = (TextView) convertView.findViewById(R.id.tv_wind);
                    weatherHeaderHolder.todayWeatherDetailTxt.setOnClickListener(clickListener);
                    weatherHeaderHolder.tomorrowWeatherDetailTxt.setOnClickListener(clickListener);
                    convertView.setTag(weatherHeaderHolder);
                    break;
                case WEATHER_AIR_BOARD_TYPE:
                    airBoradHolder = new AirBoradHolder();
                    convertView = inflater.inflate(R.layout.item_weather_air, null);
                    airBoradHolder.boardView = (BoardView) convertView.findViewById(R.id.boardView);
                    airBoradHolder.todayAirQualityTxt = (TextView) convertView.findViewById(R.id
                        .tv_today_air);
                    airBoradHolder.monthAirQualityTxt = (TextView) convertView.findViewById(R.id
                        .tv_month_air);
                    airBoradHolder.todayAirQualityTxt.setOnClickListener(clickListener);
                    airBoradHolder.monthAirQualityTxt.setOnClickListener(clickListener);
                    convertView.setTag(airBoradHolder);
                    break;
                case WEATHER_LIVE_SUGGEST_TYPE:
                    liveSuggestHolder = new LiveSuggestHolder();
                    convertView = inflater.inflate(R.layout.item_weather_live, null);
                    liveSuggestHolder.baskClotherTxt = (TextView) convertView.findViewById(R.id
                        .tv_bask_clothes);
                    liveSuggestHolder.clotherSugestTxt = (TextView) convertView.findViewById(R.id
                        .tv_clothes_suggest);
                    liveSuggestHolder.clothesImg = (ImageView) convertView.findViewById(R.id.iv_yifu);
                    liveSuggestHolder.exerciceTxt = (TextView) convertView.findViewById(R.id
                        .tv_exercise);
                    liveSuggestHolder.fishTxt = (TextView) convertView.findViewById(R.id.tv_fish);
                    liveSuggestHolder.guoMingTxt = (TextView) convertView.findViewById(R.id.tv_guomin);
                    liveSuggestHolder.illnessTxt = (TextView) convertView.findViewById(R.id
                        .tv_illness);
                    liveSuggestHolder.lunarCalerdarTxt = (TextView) convertView.findViewById(R.id
                        .tv_caleddar);
                    liveSuggestHolder.todayWeatherDescTxt = (TextView) convertView.findViewById(R.id
                        .tv_weather_today_desc);
                    liveSuggestHolder.ultravioletTxt = (TextView) convertView.findViewById(R.id
                        .tv_ultraviolet);
                    liveSuggestHolder.umbrellaTxt = (TextView) convertView.findViewById(R.id
                        .tv_umbrella);
                    convertView.setTag(liveSuggestHolder);
                    break;
            }
        } else {
            switch (type) {
                case SINGLE_ADVERT_TYPE:
                    singleAdvertViewHolder = (SingleAdvertViewHolder) convertView.getTag();
                    break;
                case MULTI_ADVERT_TYPE:
                    multiAdvertViewHolder = (MultiAdvertViewHolder) convertView.getTag();
                    break;
                case WEATHER_LIST_ITEM_TYPE:
                    weatherItemHolder = (WeatherItemHolder) convertView.getTag();
                    break;
                case WEATHER_AIR_BOARD_TYPE:
                    airBoradHolder = (AirBoradHolder) convertView.getTag();
                    break;
                case WEATHER_LIVE_SUGGEST_TYPE:
                    liveSuggestHolder = (LiveSuggestHolder) convertView.getTag();
                    break;
                case WEATHER_HAEADER_TYPE:
                    weatherHeaderHolder = (WeatherHeaderHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case WEATHER_HAEADER_TYPE:
                setWeaherHeaderData(weatherHeaderHolder, weathers.get(0));
                break;
            case WEATHER_LIST_ITEM_TYPE:
                Weather weather = weathers.get(position - 1);
                setWeathersListData(weatherItemHolder, weather);
                break;
            case WEATHER_AIR_BOARD_TYPE:
                Weather weather1 = weathers.get(0);
                airBoradHolder.boardView.setData(weather1.getAirNum());
                break;
            case WEATHER_LIVE_SUGGEST_TYPE:
                setLiveSuggestData(liveSuggestHolder, weathers.get(0));
                break;
            case SINGLE_ADVERT_TYPE:
                Advert advert1 = adverts.get(position - weathers.size() - 3);
                setSingleAdvertData(singleAdvertViewHolder, advert1);
                break;
            case MULTI_ADVERT_TYPE:
                Advert advert2 = adverts.get(position - weathers.size() - 3);
                setMultiAdvertData(multiAdvertViewHolder, advert2);
                break;
        }
        return convertView;
    }

    private class WeatherItemHolder {
        private TextView date, dateDesc, weather, tempRange;
        private ImageView weatherImg, arrowImg;
    }

    private class WeatherHeaderHolder {
        private TextView weatherDescTxt, airQualityDescTxt, currentTempTxt,
            currentWeatherDescTxt, windTxt,
            windLevelTxt, airPressureNumTxt, todayWeatherDetailTxt, tomorrowWeatherDetailTxt;
        private ImageView weaherImg, airQualityImg;
        private LinearLayout temperatureContainerLayout;
    }

    private class AirBoradHolder {
        private TextView todayAirQualityTxt, monthAirQualityTxt;
        private BoardView boardView;
    }

    private class LiveSuggestHolder {
        private ImageView clothesImg;
        private TextView todayWeatherDescTxt, clotherSugestTxt, lunarCalerdarTxt, umbrellaTxt, guoMingTxt, illnessTxt,
            baskClotherTxt, ultravioletTxt, exerciceTxt, fishTxt;
    }

    private class SingleAdvertViewHolder {
        private TextView advertContent;
        private ImageView advertImg;
    }

    private class MultiAdvertViewHolder {
        private TextView advertContent;
        private ImageView advertImg1, advertImg2, advertImg3;
    }

    private void setSingleAdvertData(SingleAdvertViewHolder singleAdvertViewHolder, Advert advert) {
        ImageAware imageAware = new ImageViewAware(singleAdvertViewHolder.advertImg, false);
        imageLoader.displayImage(advert.getImgUrlList().get(0), imageAware, options);
        singleAdvertViewHolder.advertContent.setText(advert.getContent());
    }

    private void setMultiAdvertData(MultiAdvertViewHolder multiAdvertViewHolder, Advert advert) {
        multiAdvertViewHolder.advertContent.setText(advert.getContent());
        ImageAware imageAware1 = new ImageViewAware(multiAdvertViewHolder.advertImg1, false);
        ImageAware imageAware2 = new ImageViewAware(multiAdvertViewHolder.advertImg2, false);
        ImageAware imageAware3 = new ImageViewAware(multiAdvertViewHolder.advertImg3, false);
        imageLoader.displayImage(advert.getImgUrlList().get(0), imageAware1, options);
        imageLoader.displayImage(advert.getImgUrlList().get(1), imageAware2, options);
        imageLoader.displayImage(advert.getImgUrlList().get(2), imageAware3, options);
    }

    private void setWeathersListData(WeatherItemHolder weatherItemHolder, Weather weather) {
        weatherItemHolder.dateDesc.setText(DateUtil.getWeekOfDate(weather.getDate()));
        weatherItemHolder.date.setText(DateUtil.getDate(weather.getDate()));
        weatherItemHolder.weatherImg.setImageResource(WeatherUtil.getWeatherResId(weather.getWeatherCode()));
        weatherItemHolder.weather.setText(weather.getWeatherDesc());
        weatherItemHolder.tempRange.setText(weather.getTemperatureMin() + "~" + weather.getTemperatureMax() + "℃");
        if (DateUtil.isTomorrow(weather.getDate())) {
            weatherItemHolder.arrowImg.setVisibility(View.VISIBLE);
        } else {
            weatherItemHolder.arrowImg.setVisibility(View.INVISIBLE);
        }
    }

    private void setWeaherHeaderData(WeatherHeaderHolder weaherHeaderHolder, Weather weather) {
        //设置天气描述
        weaherHeaderHolder.weatherDescTxt.setText(weather.getWeatherDesc());
        //设置温度图片
        weaherHeaderHolder.weaherImg.setImageResource(WeatherUtil.getWeatherResId(weather.getWeatherCode
            ()));
        weaherHeaderHolder.temperatureContainerLayout.removeAllViews();
        //设置温度范围
        setTem(weaherHeaderHolder.temperatureContainerLayout, weather.getTemperatureMin(), 0);
        setTem(weaherHeaderHolder.temperatureContainerLayout, weather.getTemperatureMax(), 1);
        //设置空气质量图片
        weaherHeaderHolder.airQualityImg.setImageResource(WeatherUtil.getAirPicResId(weather.getAirNum()));
        //设置空气质量级别描述
        weaherHeaderHolder.airQualityDescTxt.setText(WeatherUtil.getDes(context, weather.getAirNum
            ()));
        //设置空气质量指数
        weaherHeaderHolder.airPressureNumTxt.setText(String.valueOf(weather.getAirNum()));
        //设置当前温度
        weaherHeaderHolder.currentTempTxt.setText(weather.getCurrentTemp() + "℃");
        //设置当期天气描述
        weaherHeaderHolder.currentWeatherDescTxt.setText(weather.getCurrentWeatherDesc());
        //设置当前风向
        weaherHeaderHolder.windTxt.setText(weather.getWindDesc());
        //设置当前风力级别
        weaherHeaderHolder.windLevelTxt.setText(weather.getWindLevel() + context.getString(R.string
            .level));
        //设置当前气压
        weaherHeaderHolder.airPressureNumTxt.setText(weather.getAirNum() +
            context.getString(R.string.air_pressure_unit));
    }

    private void setTem(LinearLayout llTemperature, int temp, int type) {
        ImageView tempView = null;
        if (type == 1) {
            tempView = new ImageView(context);
            tempView.setImageResource(R.mipmap.temp_split);
            llTemperature.addView(tempView);
        }

        if (temp < 0) {
            tempView = new ImageView(context);
            tempView.setImageResource(R.mipmap.temp_minus);
            llTemperature.addView(tempView);
        }
        temp = Math.abs(temp);
        if (temp < 10) {
            tempView = new ImageView(context);
            tempView.setImageResource(getTempPicResId(temp));
            llTemperature.addView(tempView);
        } else {
            tempView = new ImageView(context);
            tempView.setImageResource(getTempPicResId(temp / 10));
            llTemperature.addView(tempView);

            tempView = new ImageView(context);
            tempView.setImageResource(getTempPicResId(temp % 10));
            llTemperature.addView(tempView);
        }
        if (type == 1) {
            tempView = new ImageView(context);
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

    private void setLiveSuggestData(LiveSuggestHolder liveSuggestHolder, Weather weather) {
        if (weather.getTemperatureMax() < 5) {
            liveSuggestHolder.clothesImg.setImageResource(R.mipmap.live_bu_shufu);
            liveSuggestHolder.clotherSugestTxt.setText("建议穿棉衣等冬装");
        } else if (weather.getTemperatureMax() < 10) {
            liveSuggestHolder.clothesImg.setImageResource(R.mipmap.live_cool);
            liveSuggestHolder.clotherSugestTxt.setText("建议穿毛衣、夹克等厚衣服");
        } else if (weather.getTemperatureMax() < 25) {
            liveSuggestHolder.clothesImg.setImageResource(R.mipmap.live_shufu);
            liveSuggestHolder.clotherSugestTxt.setText("建议穿薄外套");
        } else {
            liveSuggestHolder.clothesImg.setImageResource(R.mipmap.live_hot);
            liveSuggestHolder.clotherSugestTxt.setText("建议穿T恤、短裤");
        }

        if (weather.getTemperatureMax() - weather.getTemperatureMin() < 10) {
            liveSuggestHolder.todayWeatherDescTxt.setText("今天气温变化平均");
        } else {
            liveSuggestHolder.todayWeatherDescTxt.setText("今天气温变化较大");
        }

        liveSuggestHolder.lunarCalerdarTxt.setText(LunarUtil.getLunarDate(weather.getDate()));

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
            || "a64".equals(weather.getWeatherCode())) {
            liveSuggestHolder.umbrellaTxt.setText("需带伞");
        } else {
            liveSuggestHolder.umbrellaTxt.setText("无需带伞");
        }

        if (weather.getAirNum() <= 150) {
            liveSuggestHolder.guoMingTxt.setText("不易过敏");
        } else {
            liveSuggestHolder.guoMingTxt.setText("易过敏");
        }

        if (weather.getTemperatureMax() < 15) {
            liveSuggestHolder.illnessTxt.setText("较易发感冒");
        } else {
            liveSuggestHolder.illnessTxt.setText("不易发感冒");
        }

        if ("a32".equals(weather.getWeatherCode()) && weather.getTemperatureMax() > 30) {
            if (weather.getTemperatureMax() < 35) {
                liveSuggestHolder.ultravioletTxt.setText("紫外线较强");
            } else if (weather.getTemperatureMax() < 35) {
                liveSuggestHolder.ultravioletTxt.setText("紫外线强");
            } else {
                liveSuggestHolder.ultravioletTxt.setText("紫外线很强");
            }
        } else {
            liveSuggestHolder.ultravioletTxt.setText("紫外线较弱");
        }

        if (("a28".equals(weather.getWeatherCode()) || "a32".equals(weather
            .getWeatherCode())) && weather
            .getTemperatureMax() > 10) {
            liveSuggestHolder.baskClotherTxt.setText("较宜晾晒");
        } else {
            liveSuggestHolder.baskClotherTxt.setText("不宜晾晒");
        }

        if (weather.getTemperatureMin() < 5 || weather.getAirNum() > 150) {
            liveSuggestHolder.exerciceTxt.setText("不宜晨练");
        } else {
            liveSuggestHolder.exerciceTxt.setText("较宜晨练");
        }

        if ("a32".equals(weather.getWeatherCode()) && weather.getTemperatureMax() > 15) {
            liveSuggestHolder.fishTxt.setText("较适宜钓鱼");
        } else {
            liveSuggestHolder.fishTxt.setText("不适宜钓鱼");
        }
    }

}
