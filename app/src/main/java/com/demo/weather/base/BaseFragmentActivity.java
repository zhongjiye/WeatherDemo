package com.demo.weather.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.demo.weather.util.AppManager;


public abstract class BaseFragmentActivity extends FragmentActivity {

    protected Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(mContext);
    }


}
