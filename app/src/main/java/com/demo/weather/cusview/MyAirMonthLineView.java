package com.demo.weather.cusview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.weather.R;
import com.demo.weather.bean.DaysAir;
import com.demo.weather.bean.MonthAir;
import com.demo.weather.util.DensityUtil;

import java.util.ArrayList;

/**
 * 15天空气指数自定义控件
 */
public class MyAirMonthLineView extends HorizontalScrollView {
    private Context context;
    private RelativeLayout container;
    private AirMonthLineView airMonthLineView;


    public MyAirMonthLineView(Context context) {
        super(context);
        init(context);
    }

    public MyAirMonthLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyAirMonthLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        container = new RelativeLayout(context);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT));

        airMonthLineView = new AirMonthLineView(context);
        airMonthLineView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        container.addView(airMonthLineView);
        addView(container);
    }

    public void setData(ArrayList<MonthAir> airs) {
        airMonthLineView.setData(airs);
    }


}