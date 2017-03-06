package com.demo.weather.cusview;

import com.demo.weather.util.DensityUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by zhongjy on 2017/3/6.
 */

public class MyScrollView extends ScrollView {
    private Context mContext;
    private LinearLayout mHeader;

    public MyScrollView(Context context) {
        super(context);
        init(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        mContext = context;
    }

    public void setHeader(LinearLayout linearLayout) {
        mHeader = linearLayout;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float ratio = (float) t / (getMeasuredHeight() - 2 * DensityUtil.dip2px(mContext, 65));
        float alpha = ratio * (255 * 0.5f) + 255 * 0.5f;
        if (mHeader != null) {
            mHeader.setBackgroundColor(Color.argb((int) alpha, 37, 97, 118));
        }
        setBackgroundColor(Color.argb((int) alpha, 37, 97, 118));
    }
}
