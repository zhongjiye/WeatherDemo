package com.demo.weather.cusview;

import com.demo.weather.util.DensityUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhongjy on 2017/3/10.
 */

@SuppressLint("AppCompatCustomView")
public class TitleTextView extends TextView {
    private LinearLayout.LayoutParams mParams;
    private int mPositionIndex;
    private int mChildPositionIndex;
    private int mMarginRight;

    public int getPositionIndex() {
        return mPositionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        mPositionIndex = positionIndex;
    }

    public int getChildPositionIndex() {
        return mChildPositionIndex;
    }

    public void setChildPositionIndex(int childPositionIndex) {
        mChildPositionIndex = childPositionIndex;
    }

    public TitleTextView(Context context, int index) {
        super(context);
        init(context, index);
    }

    public TitleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context, int index) {
        mChildPositionIndex = index;
        mPositionIndex = mChildPositionIndex + 1;
        mMarginRight = DensityUtil.dip2px(context, 15);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(0, 0, mMarginRight, 0);
        setSingleLine(true);
        setLayoutParams(mParams);
        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
        setTextSize(16);
    }

    public void setMarginLeft(int marginLeft) {
        mParams.setMargins(marginLeft, 0, mMarginRight, 0);
        setLayoutParams(mParams);
    }

    public int getMarginRight() {
        return mMarginRight;
    }

}
