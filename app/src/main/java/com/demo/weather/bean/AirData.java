package com.demo.weather.bean;

/**
 * Created by zhongjy on 2017/3/7.
 */

public class AirData {
    private String observeSite;
    private int airNum;
    private int pmNum;

    public AirData(String observeSite, int airNum, int pmNum) {
        this.observeSite = observeSite;
        this.airNum = airNum;
        this.pmNum = pmNum;
    }

    public String getObserveSite() {
        return observeSite;
    }

    public void setObserveSite(String observeSite) {
        this.observeSite = observeSite;
    }

    public int getAirNum() {
        return airNum;
    }

    public void setAirNum(int airNum) {
        this.airNum = airNum;
    }

    public int getPmNum() {
        return pmNum;
    }

    public void setPmNum(int pmNum) {
        this.pmNum = pmNum;
    }
}
