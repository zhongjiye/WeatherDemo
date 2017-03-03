package com.demo.weather.bean;

import com.amap.api.location.AMapLocation;

/**
 * Created by zhongjy on 2017/2/23.
 */

public class Location {
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;

    public Location(String country, String province, String city, String district, String street) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
