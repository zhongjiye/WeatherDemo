package com.demo.weather.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhongjy on 2017/2/24.
 */

public class DateUtil {

    public static String getSomeDay(Date date,int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(calendar.getTime());
        return time;
    }

    public static String getSomeDay(Date date,int days,String rule) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat format = new SimpleDateFormat(rule);
        String time=format.format(calendar.getTime());
        return time;
    }

    public static boolean sameDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date()).equals(time);
    }

    public static boolean isTomorrow(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(time);
            if (date.getTime() - (new Date()).getTime() < 1000 * 60 * 60 * 24) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String getDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getDate(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDate(Date date,String rule) {
        SimpleDateFormat format = new SimpleDateFormat(rule);
        return format.format(date);
    }

    public static int getDateMonth(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(time));
            return (calendar.get(Calendar.MONTH) + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDateDay(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(time));
            return (calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getWeekOfDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (sameDate(time)) {
                return "今天";
            } else if (isTomorrow(time)) {
                return "明天";
            } else {
                String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
                String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
                Date date = format.parse(time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                return weekDaysName[intWeek];
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
