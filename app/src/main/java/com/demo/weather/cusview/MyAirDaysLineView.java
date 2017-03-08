package com.demo.weather.cusview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.weather.R;
import com.demo.weather.bean.DaysAir;
import com.demo.weather.bean.MonthAir;
import com.demo.weather.util.DateUtil;
import com.demo.weather.util.DensityUtil;
import com.demo.weather.util.WeatherUtil;

import java.util.ArrayList;

/**
 * 48小时空气指数自定义控件
 */
public class MyAirDaysLineView extends HorizontalScrollView {
    private Context context;
    private RelativeLayout container;
    private AirDaysLineView airDaysLineView;
    private TextView desTextView;

    public MyAirDaysLineView(Context context) {
        super(context);
        init(context);
    }

    public MyAirDaysLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyAirDaysLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        container = new RelativeLayout(context);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
            .LayoutParams.WRAP_CONTENT));

        airDaysLineView = new AirDaysLineView(context);
        airDaysLineView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT));

        desTextView = new TextView(context);
        desTextView.setLayoutParams(new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 50), DensityUtil
            .dip2px(context, 20)));
        desTextView.setGravity(Gravity.CENTER);
        desTextView.setBackgroundResource(R.drawable.air_num_bg);
        desTextView.setTextSize(10);
        desTextView.setTextColor(Color.WHITE);

        container.addView(airDaysLineView);
        container.addView(desTextView);

        addView(container);
    }

    public void setData(ArrayList<DaysAir> airs) {
        airDaysLineView.setData(airs);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutDesTextView(0);
            }
        }, 300);
    }


    private void layoutDesTextView(int scrollX) {
        float max = airDaysLineView.getWidth() - getWidth();
        float maxScroll = (airDaysLineView.getWidth() - airDaysLineView.getExtra() - desTextView.getWidth());
        int x = (int) (scrollX * (maxScroll / max));
        int[] nums = airDaysLineView.getTextHeight(x);
        GradientDrawable myGrad = (GradientDrawable) desTextView.getBackground();
        myGrad.setColor(airDaysLineView.getColor(nums[1]));
        desTextView.setText(nums[1] + " " + WeatherUtil.getDes(context, nums[1], 0));
        desTextView.layout(x + desTextView.getWidth() / 2,
            nums[0] - desTextView.getHeight() - 15,//
            (int) (x + desTextView.getWidth() * 1.5),//
            nums[0] - 15);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        layoutDesTextView(l);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


}