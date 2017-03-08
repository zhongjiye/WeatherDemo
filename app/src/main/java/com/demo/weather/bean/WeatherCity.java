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
    }

    public static final Parcelable.Creator<WeatherCity> CREATOR = new Parcelable.Creator<WeatherCity>() {
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
