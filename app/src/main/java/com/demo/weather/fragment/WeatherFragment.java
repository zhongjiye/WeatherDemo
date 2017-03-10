package com.demo.weather.fragment;

import com.alibaba.fastjson.JSON;
import com.demo.weather.R;
import com.demo.weather.activity.AddCityActivity;
import com.demo.weather.activity.MainActivity;
import com.demo.weather.adapter.HeaderTitleAdapter;
import com.demo.weather.adapter.ViewPagerAdapter;
import com.demo.weather.bean.WeatherCity;
import com.demo.weather.cusview.TitleTextView;
import com.demo.weather.cusview.ViewPagerScroller;
import com.demo.weather.util.DensityUtil;
import com.demo.weather.util.SharedPreferencesUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.demo.weather.config.BroadCastReceiverConfig.UPDATE_CITYLIST;
import static com.demo.weather.config.SharePreferenceConfig.WEATHER_CITY_LIST_TAG;

/**
 * 天气主界面
 */
public class WeatherFragment extends Fragment implements WeatherDetailFragment
    .WeatherDetailMessage, ViewPager
    .OnPageChangeListener, View.OnClickListener {

    @InjectView(R.id.vp_main)
    ViewPager mVpMain;
    @InjectView(R.id.iv_toggle)
    ImageView mIvToggle;
    @InjectView(R.id.iv_add_city)
    ImageView mIvAddCity;
    @InjectView(R.id.iv_search_city)
    ImageView mIvSearchCity;
    @InjectView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @InjectView(R.id.ll_title)
    LinearLayout mIndicateLayout;
    @InjectView(R.id.indicate_scroll)
    HorizontalScrollView mIndicateScroll;

    private Context mContext;


    public interface WeatherMessage {
        /**
         * 切换侧滑菜单开闭状态
         */
        void toggleLeftMenu();
    }


    private int index;
    private ArrayList<WeatherDetailFragment> fragmentList;

    private ViewPagerAdapter adapter;

    private WeatherMessage weatherMessage;
    private List<WeatherCity> weatherCityList;


    private MyReceiver myReceiver;

    private List<TitleTextView> mTitleTextViews = new ArrayList<>();

    public WeatherFragment() {

    }

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.inject(this, view);
        init();
        return view;
    }


    private void init() {
        mContext = getContext();
        myReceiver = new MyReceiver();
        getContext().registerReceiver(myReceiver, new IntentFilter(UPDATE_CITYLIST));
        weatherCityList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        String content = String.valueOf(SharedPreferencesUtils.get(getContext(),
            WEATHER_CITY_LIST_TAG, ""));
        if (!TextUtils.isEmpty(content)) {
            List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
            if (list != null && list.size() != 0) {
                weatherCityList.addAll(list);
            }
        }
        for (WeatherCity temp : weatherCityList) {
            fragmentList.add(WeatherDetailFragment.newInstance(this, temp.getZhongwen(), temp.isLocate()));
        }

        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mVpMain.setAdapter(adapter);
        mVpMain.addOnPageChangeListener(this);
        mVpMain.setOffscreenPageLimit(2);
        ViewPagerScroller mPagerScroller = new ViewPagerScroller(getActivity());
        mPagerScroller.initViewPagerScroll(mVpMain);


        initGuide();

    }

    private void initGuide() {
        mIndicateScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setCircleCount(fragmentList.size());
        circleNavigator.setCircleColor(Color.WHITE);
        mMagicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mVpMain);
        adapter.notifyDataSetChanged();

        mTitleTextViews.clear();
        mIndicateLayout.removeAllViews();
        for (int i = 0; i < weatherCityList.size(); i++) {
            TitleTextView titleTextView = new TitleTextView(getContext(), i);
            titleTextView.setText(weatherCityList.get(i).getZhongwen());
            titleTextView.setOnClickListener(WeatherFragment.this);
            if (i > 0) {
                titleTextView.setTextSize(12);
                titleTextView.setAlpha(0.7f);
            } else {
                titleTextView.setMarginLeft(DensityUtil.dip2px(getContext(), 65) - (int)
                    getTextSize(16, weatherCityList.get(i).getZhongwen()) / 2);
            }
            mTitleTextViews.add(titleTextView);
            mIndicateLayout.addView(titleTextView);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position < 0 || position > fragmentList.size() - 1 || index == position) {
            return;
        }
        index = position;
        if (!clickFlg) {
            animator(mTitleTextViews.get(index));
        }
        clickFlg = false;
    }


    public void selectPage(int index) {
        mVpMain.setCurrentItem(index, true);
    }


    @Override
    public void setDistrict(String text) {
//        tvLocationDistrict.setText(text);
    }

    @OnClick({R.id.iv_toggle, R.id.iv_add_city, R.id.iv_search_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tomorrow_detail:
                ((MainActivity) getContext()).setSelectTab(2);
                break;
            case R.id.tv_today_detail:
                ((MainActivity) getContext()).setSelectTab(1);
                break;
            case R.id.iv_toggle:
                if (weatherMessage != null) {
                    weatherMessage.toggleLeftMenu();
                }
                break;
            case R.id.iv_add_city:
                startActivity(new Intent(getContext(), AddCityActivity.class));
                break;
            case R.id.iv_search_city:
                break;
            default:
                clickFlg = true;
                animator((TitleTextView) view);
                break;
        }
    }


    private float getTextSize(int textSize, String content) {
        TextPaint newPaint = new TextPaint();
        newPaint.setTextSize(getResources().getDisplayMetrics().scaledDensity * textSize);
        return newPaint.measureText(content);
    }

    private boolean clickFlg = false;

    private void animator(TitleTextView view) {
        int postionIndex = view.getPositionIndex();
        if (postionIndex != 1) {
            if (clickFlg) {
                selectPage(view.getChildPositionIndex());
            }
            int childIndex = view.getChildPositionIndex();

            float move = 0;
            if (postionIndex > 1) {//左移
                move = view.getWidth() / (float) 2
                    + mTitleTextViews.get(childIndex - 1).getWidth() / (float) 2
                    + view.getMarginRight() - DensityUtil.sp2px(getContext(), 3f);
            } else {//右移
                move = view.getWidth() / (float) 2
                    + mTitleTextViews.get(childIndex + 1).getWidth() / (float) 2
                    + view.getMarginRight() - DensityUtil.sp2px(getContext(), 3f);
            }

            for (int j = 0; j < mTitleTextViews.size(); j++) {
                AnimatorSet animationSet = new AnimatorSet();
                animationSet.setInterpolator(new LinearInterpolator());
                animationSet.setDuration(200);
                TitleTextView currentView = mTitleTextViews.get(j);
                currentView.setPivotX(currentView.getWidth() / 2);
                currentView.setPivotY(currentView.getHeight() / 2);
                int currentChildIndex = currentView.getChildPositionIndex();
                int currentPositionIndex = currentView.getPositionIndex();

                animationSet.playTogether(//
                    getAnimator(currentView, currentChildIndex == childIndex ? ALPHA_LIGHTER :
                        ALPHA_DARKER, 0),
                    getAnimator(currentView, postionIndex > 1 ? TRANSLATE_LEFT : TRANSLATE_RIGHT,
                        move));
                currentView.setPositionIndex(postionIndex > 1 ? currentPositionIndex - 1 : currentPositionIndex + 1);
                currentView.setTextSize(currentChildIndex == childIndex ? 16 : 13);
                animationSet.start();
            }
        }
    }

    private final int TRANSLATE_LEFT = 0;
    private final int TRANSLATE_RIGHT = 1;
    private final int ALPHA_LIGHTER = 2;
    private final int ALPHA_DARKER = 3;

    private Animator getAnimator(View view, int type, float move) {
        Animator animator = null;
        switch (type) {
            case TRANSLATE_LEFT:
                animator = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(),
                    view.getTranslationX() - move);
                break;
            case TRANSLATE_RIGHT:
                animator = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(),
                    view.getTranslationX() + move);
                break;
            case ALPHA_LIGHTER:
                animator = ObjectAnimator.ofFloat(view, "alpha", 0.7f, 1.0f);
                break;
            case ALPHA_DARKER:
                animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.7f);
                break;
        }
        return animator;
    }

    private boolean initFlag = false;

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UPDATE_CITYLIST:
                    String content = String.valueOf(SharedPreferencesUtils.get(getContext(),
                        WEATHER_CITY_LIST_TAG, ""));
                    if (!TextUtils.isEmpty(content)) {
                        List<WeatherCity> list = JSON.parseArray(content, WeatherCity.class);
                        if (list != null && list.size() != 0) {
                            weatherCityList.clear();
                            weatherCityList.addAll(list);
                            fragmentList.clear();
                            for (WeatherCity temp : weatherCityList) {
                                fragmentList.add(WeatherDetailFragment.newInstance
                                    (WeatherFragment.this, temp
                                        .getZhongwen(), temp.isLocate()));
                            }
                            adapter.notifyDataSetChanged();
                            initFlag = true;
                            initGuide();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getContext() instanceof WeatherMessage) {
            this.weatherMessage = (WeatherMessage) getContext();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(myReceiver);
    }
}