package com.demo.weather.bean;

/**
 * Created by zhongjy on 2017/2/27.
 */

public class MonthAir {

    private String day;
    private int airNum;

    public MonthAir(String day, int airNum) {
        this.day = day;
        this.airNum = airNum;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getAirNum() {
        return airNum;
    }

    public void setAirNum(int airNum) {
        this.airNum = airNum;
    }
}
