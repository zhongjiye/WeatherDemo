/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;



import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;
import com.nineoldandroids.animation.ObjectAnimator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class RotateLoadingLayout extends LoadingLayout {

    static final int ROTATION_ANIMATION_DURATION = 1200;

    private final ObjectAnimator mRotateAnimation;

    private Context mContext;


    private final boolean mRotateDrawableWhilePulling;

    public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mContext = context;
        mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
        mRotateAnimation = ObjectAnimator.ofFloat(mHeaderImage, "rotation", 0, 360).setDuration
            (1500);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatMode(Animation.RESTART);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mHeaderImage.setPivotX(dip2px(context, 25) / 2);
        mHeaderImage.setPivotY(dip2px(context, 25) / 2);
    }

    public void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void refreshingImpl() {
        mRotateAnimation.start();
    }

    @Override
    protected void resetImpl() {
        if (mRotateAnimation != null) {
            mRotateAnimation.cancel();
        }
        mHeaderImage.clearAnimation();
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }


    @Override
    protected void roateSunImg(float scrollVlaue) {
        float degree = lastEndDegree - (scrollVlaue - lastScrollValue);
        ObjectAnimator animation = ObjectAnimator.ofFloat(mHeaderImage, "rotation", lastEndDegree,
            degree)
            .setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        mHeaderImage.setPivotX(dip2px(mContext, 25) / 2);
        mHeaderImage.setPivotY(dip2px(mContext, 25) / 2);
        animation.start();
        lastEndDegree = degree;
        lastScrollValue = scrollVlaue;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
