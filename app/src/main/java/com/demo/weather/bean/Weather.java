package com.demo.weather.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.weather.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import butterknife.InjectView;

/**
 * Created by zhongjy on 2017/2/22.
 */

public class Weather implements Cloneable, Parcelable {
    private String weatherDesc;
    private String weatherCode;

    private int temperatureMin;
    private int temperatureMax;

    private int currentTemp;
    private String currentWeatherDesc;

    private int windLevel;
    private String windDesc;

    private int airPressure;

    private int airNum;

    private String date;

    private int humidity;
    private String sunup;
    private String sunset;
    private String ultraviolet;

    public Weather(String weatherDesc, String weatherCode, int temperatureMin, int temperatureMax, int currentTemp, String currentWeatherDesc, int windLevel, String windDesc, int airPressure, int airNum, String date, int humidity, String sunup, String sunset, String ultraviolet) {
        this.weatherDesc = weatherDesc;
        this.weatherCode = weatherCode;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.currentTemp = currentTemp;
        this.currentWeatherDesc = currentWeatherDesc;
        this.windLevel = windLevel;
        this.windDesc = windDesc;
        this.airPressure = airPressure;
        this.airNum = airNum;
        this.date = date;
        this.humidity = humidity;
        this.sunup = sunup;
        this.sunset = sunset;
        this.ultraviolet = ultraviolet;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getSunup() {
        return sunup;
    }

    public void setSunup(String sunup) {
        this.sunup = sunup;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getUltraviolet() {
        return ultraviolet;
    }

    public void setUltraviolet(String ultraviolet) {
        this.ultraviolet = ultraviolet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public int getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(int airPressure) {
        this.airPressure = airPressure;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public int getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(int temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public int getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(int temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getCurrentWeatherDesc() {
        return currentWeatherDesc;
    }

    public void setCurrentWeatherDesc(String currentWeatherDesc) {
        this.currentWeatherDesc = currentWeatherDesc;
    }

    public int getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(int windLevel) {
        this.windLevel = windLevel;
    }

    public String getWindDesc() {
        return windDesc;
    }

    public void setWindDesc(String windDesc) {
        this.windDesc = windDesc;
    }

    public int getAirNum() {
        return airNum;
    }

    public void setAirNum(int airNum) {
        this.airNum = airNum;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weatherDesc);
        dest.writeString(this.weatherCode);
        dest.writeInt(this.temperatureMin);
        dest.writeInt(this.temperatureMax);
        dest.writeInt(this.currentTemp);
        dest.writeString(this.currentWeatherDesc);
        dest.writeInt(this.windLevel);
        dest.writeString(this.windDesc);
        dest.writeInt(this.airPressure);
        dest.writeInt(this.airNum);
        dest.writeString(this.date);
        dest.writeInt(this.humidity);
        dest.writeString(this.sunup);
        dest.writeString(this.sunset);
        dest.writeString(this.ultraviolet);
    }

    protected Weather(Parcel in) {
        this.weatherDesc = in.readString();
        this.weatherCode = in.readString();
        this.temperatureMin = in.readInt();
        this.temperatureMax = in.readInt();
        this.currentTemp = in.readInt();
        this.currentWeatherDesc = in.readString();
        this.windLevel = in.readInt();
        this.windDesc = in.readString();
        this.airPressure = in.readInt();
        this.airNum = in.readInt();
        this.date = in.readString();
        this.humidity = in.readInt();
        this.sunup = in.readString();
        this.sunset = in.readString();
        this.ultraviolet = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
