package com.demo.weather.bean;

import java.util.List;

/**
 * Created by zhongjy on 2017/2/27.
 */

public class DaysAir {

    private int clock;
    private int airNum;

    public DaysAir(int clock, int airNum) {
        this.clock = clock;
        this.airNum = airNum;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getAirNum() {
        return airNum;
    }

    public void setAirNum(int airNum) {
        this.airNum = airNum;
    }
}
