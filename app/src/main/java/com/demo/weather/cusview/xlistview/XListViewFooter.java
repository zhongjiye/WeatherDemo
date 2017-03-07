/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.demo.weather.cusview.xlistview;


import com.demo.weather.R;
import com.demo.weather.util.DensityUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mContentView;
    private ImageView mArrowImgView, cirCleImgView;
    private TextView mHintView;

    private Animation mRoateAnimation;

    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mArrowImgView = (ImageView) moreView.findViewById(R.id.xlistview_footer_arrow);
        cirCleImgView = (ImageView) moreView.findViewById(R.id.xlistview_footer_circle);
        mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);

        mRoateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRoateAnimation.setFillAfter(true); // 设置保持动画最后的状态
        mRoateAnimation.setDuration(2000); // 设置动画时间
        mRoateAnimation.setRepeatMode(Animation.INFINITE);
        mRoateAnimation.setInterpolator(new LinearInterpolator()); // 设置插入器
    }

    public void setState(int state) {
        if (state == STATE_READY) {
            cirCleImgView.clearAnimation();
            cirCleImgView.setVisibility(View.INVISIBLE);
            mArrowImgView.setImageResource(R.mipmap.ic_pull_down);
            mArrowImgView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_ready);
        } else if (state == STATE_LOADING) {
            cirCleImgView.startAnimation(mRoateAnimation);
            mArrowImgView.setVisibility(View.INVISIBLE);
            cirCleImgView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_loading);
        } else {
            mArrowImgView.setImageResource(R.mipmap.ic_pull_up);
            mArrowImgView.setVisibility(View.VISIBLE);
            cirCleImgView.clearAnimation();
            cirCleImgView.setVisibility(View.INVISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_normal);
        }
    }

    public void setBottomMargin(int height) {
        if (height < 0) return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }


    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }


}
