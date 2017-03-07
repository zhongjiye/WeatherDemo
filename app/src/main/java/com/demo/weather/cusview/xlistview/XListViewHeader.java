/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.demo.weather.cusview.xlistview;


import com.demo.weather.R;
import com.demo.weather.util.DensityUtil;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XListViewHeader extends LinearLayout {
    private Context mContext;
    private LinearLayout mContainer;
    private ImageView mSunImgView;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;


    private ObjectAnimator mRotateAnimation;


    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(
            LayoutParams.FILL_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
            R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mSunImgView = (ImageView) findViewById(R.id.iv_sun);

        mRotateAnimation = ObjectAnimator.ofFloat(mSunImgView, "rotation",
            0, 360)
            .setDuration
                (1500);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mSunImgView.setPivotX(DensityUtil.dip2px(context, 25) / 2);
        mSunImgView.setPivotY(DensityUtil.dip2px(context, 25) / 2);
    }

    public void setState(int state) {
        if (state == mState) return;
        switch (state) {
            case STATE_NORMAL:
                mRotateAnimation.cancel();
                mSunImgView.clearAnimation();
                mHintTextView.setText(R.string.xlistview_header_hint_normal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mHintTextView.setText(R.string.xlistview_header_hint_ready);
                }
                break;
            case STATE_REFRESHING:
                mRotateAnimation.start();
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
                break;
            default:
        }

        mState = state;
    }

    private float lastEndDegree = 0;

    public void setScrollHeader(float scrollY) {
        if (scrollY != 0) {
            float degree = lastEndDegree + scrollY;
            ObjectAnimator animation = ObjectAnimator.ofFloat(mSunImgView, "rotation", lastEndDegree,
                degree)
                .setDuration(400);
            animation.setInterpolator(new LinearInterpolator());
            mSunImgView.setPivotX(DensityUtil.dip2px(mContext, 25) / 2);
            mSunImgView.setPivotY(DensityUtil.dip2px(mContext, 25) / 2);
            animation.start();
            lastEndDegree = degree;
        }
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer
            .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getLayoutParams().height;
    }

}
