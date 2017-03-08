package com.demo.weather.cusview;

import com.demo.weather.R;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.util.ColorEvaluator;
import com.demo.weather.util.DensityUtil;
import com.demo.weather.util.WeatherUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhongjy on 2017/3/7.
 */

public class MyHeaderTitleView extends LinearLayout {
    private TextView mTitleTextView;
    private ImageView mLocateImageView;


    public MyHeaderTitleView(Context context) {
        super(context);
        init(context);
    }

    public MyHeaderTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyHeaderTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);

        mLocateImageView = new ImageView(context);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
        params1.rightMargin = DensityUtil.dip2px(context, 2);
        mLocateImageView.setLayoutParams(params1);
        mLocateImageView.setImageResource(R.mipmap.icon_location_menu);
        mLocateImageView.setVisibility(View.INVISIBLE);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
        mTitleTextView = new TextView(context);
        mTitleTextView.setLayoutParams(params2);
        mTitleTextView.setTextSize(16);
        mTitleTextView.setTextColor(Color.WHITE);

        addView(mLocateImageView);
        addView(mTitleTextView);

    }

    public void setData(WeatherCity weatherCity) {
        if (weatherCity != null) {
            mTitleTextView.setText(weatherCity.getZhongwen());
            if (weatherCity.isLocate()) {
                mLocateImageView.setVisibility(View.VISIBLE);
            } else {
                mLocateImageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void scaleView(boolean narrow) {
        if (narrow) {
            mTitleTextView.setTextSize(12);
            mTitleTextView.setTextColor(Color.parseColor("#77FFFFFF"));
        } else {
            mTitleTextView.setTextSize(16);
            mTitleTextView.setTextColor(Color.WHITE);
        }
    }


}
