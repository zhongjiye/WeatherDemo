package com.demo.weather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhongjy on 2017/3/6.
 */

public class WeatherCity implements Parcelable {
    private boolean isLocate;
    private boolean isDefault;
    private Weather weather;
    private String zhongwen;
    private String pinyin;
    private String py;
    private String id;
    private String quhao;

    private String name;
    private String english;
    private String area;
    private String country;


    public WeatherCity() {
    }

    public WeatherCity(boolean isLocate, boolean isDefault, String zhongwen, Weather weather) {
        this.isLocate = isLocate;
        this.isDefault = isDefault;
        this.weather = weather;
        this.zhongwen = zhongwen;
    }

    public boolean isLocate() {
        return isLocate;
    }

    public void setLocate(boolean locate) {
        isLocate = locate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }


    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getZhongwen() {
        return zhongwen;
    }

    public void setZhongwen(String zhongwen) {
        this.zhongwen = zhongwen;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuhao() {
        return quhao;
    }

    public void setQuhao(String quhao) {
        this.quhao = quhao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLocate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.weather, flags);
        dest.writeString(this.zhongwen);
        dest.writeString(this.pinyin);
        dest.writeString(this.py);
        dest.writeString(this.id);
        dest.writeString(this.quhao);
        dest.writeString(this.name);
        dest.writeString(this.english);
        dest.writeString(this.area);
        dest.writeString(this.country);
    }

    protected WeatherCity(Parcel in) {
        this.isLocate = in.readByte() != 0;
        this.isDefault = in.readByte() != 0;
        this.weather = in.readParcelable(Weather.class.getClassLoader());
        this.zhongwen = in.readString();
        this.pinyin = in.readString();
        this.py = in.readString();
        this.id = in.readString();
        this.quhao = in.readString();
        this.name = in.readString();
        this.english = in.readString();
        this.area = in.readString();
        this.country = in.readString();
    }

    public static final Creator<WeatherCity> CREATOR = new Creator<WeatherCity>() {
        @Override
        public WeatherCity createFromParcel(Parcel source) {
            return new WeatherCity(source);
        }

        @Override
        public WeatherCity[] newArray(int size) {
            return new WeatherCity[size];
        }
    };
}
