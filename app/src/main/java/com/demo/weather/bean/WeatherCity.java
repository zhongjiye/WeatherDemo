package com.demo.weather.bean;

/**
 * Created by zhongjy on 2017/3/6.
 */

public class WeatherCity {
    private boolean isLocate;
    private boolean isDefault;
    private String city;
    private Weather weather;

    public WeatherCity() {
    }

    public WeatherCity(boolean isLocate, boolean isDefault, String city, Weather weather) {
        this.isLocate = isLocate;
        this.isDefault = isDefault;
        this.city = city;
        this.weather = weather;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
