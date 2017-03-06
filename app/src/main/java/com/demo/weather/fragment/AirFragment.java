package com.demo.weather.fragment;

import com.demo.weather.R;
import com.demo.weather.bean.DaysAir;
import com.demo.weather.bean.MonthAir;
import com.demo.weather.cusview.BoardMoceView;
import com.demo.weather.cusview.MyAirDaysLineView;
import com.demo.weather.cusview.MyAirMonthLineView;
import com.demo.weather.cusview.MyScrollView;
import com.demo.weather.util.DateUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 空气质量界面
 */
public class AirFragment extends Fragment {


    @InjectView(R.id.boardView)
    BoardMoceView boardView;
    @InjectView(R.id.tv_location_district)
    TextView tvLocationDistrict;
    @InjectView(R.id.tv_mask)
    TextView tvMask;
    @InjectView(R.id.tv_exercise)
    TextView tvExercise;
    @InjectView(R.id.tv_outdoor)
    TextView tvOutdoor;
    @InjectView(R.id.tv_window)
    TextView tvWindow;
    @InjectView(R.id.tv_pm)
    TextView tvPm;
    @InjectView(R.id.tv_pm10)
    TextView tvPm10;
    @InjectView(R.id.tv_so)
    TextView tvSo;
    @InjectView(R.id.tv_no)
    TextView tvNo;
    @InjectView(R.id.tv_co)
    TextView tvCo;
    @InjectView(R.id.tv_o)
    TextView tvO;
    @InjectView(R.id.tt_days)
    TextView ttDays;
    @InjectView(R.id.tt_month)
    TextView ttMonth;
    @InjectView(R.id.my_air_days_line)
    MyAirDaysLineView myAirDaysLine;
    @InjectView(R.id.my_air_month_line)
    MyAirMonthLineView myAirMonthLine;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.ms_scroll)
    MyScrollView mMsScroll;
    @InjectView(R.id.ll_header)
    LinearLayout mLlHeader;
    @InjectView(R.id.iv_arrow)
    ImageView mIvArrow;
    @InjectView(R.id.activity_main)
    RelativeLayout mActivityMain;


    private Random random;

    public AirFragment() {

    }

    public static AirFragment newInstance() {
        AirFragment fragment = new AirFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_air, container, false);
        ButterKnife.inject(this, view);
        init();
        return view;
    }

    private void init() {
        mLlHeader.setBackgroundColor(Color.argb((int) (255 * 0.5f), 37, 97, 118));
        mMsScroll.setBackgroundColor(Color.argb((int) (255 * 0.5f), 37, 97, 118));
        mMsScroll.setHeader(mLlHeader);
        random = new Random();
        boardView.setData(193);
        initDaysAirData();
        initMonthAirData();
    }


    /**
     * 设置48小时空气质量预报
     */
    private void initDaysAirData() {
        ArrayList<DaysAir> airs = new ArrayList<>();
        for (int i = 0, clock = 14, j = 350; i < 48; i++, clock++, j = j + (i % 2 == 0 ? 20 :
            -30)) {
            airs.add(new DaysAir(clock % 24, j));
        }
        myAirDaysLine.setData(airs);
    }

    /**
     * 设置15天空气质量预报
     */
    private void initMonthAirData() {
        ArrayList<MonthAir> monthAirs = new ArrayList<>();
        for (int i = 0, j = 400; i < 15; i++, j = j + (i % 2 == 0 ? 20 : -45)) {
            monthAirs.add(new MonthAir(DateUtil.getSomeDay(new Date(), i), j));
        }
        myAirMonthLine.setData(monthAirs);
    }

    /**
     * 获取介于min和max之间的随机整数
     *
     * @param min 范围最小值
     * @param max 范围最大数值
     */
    private int getRandom(Random random, int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }


    @OnClick({R.id.tt_days, R.id.tt_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tt_days:
                ttDays.setBackgroundResource(R.drawable.button_bg_left_press);
                ttMonth.setBackgroundResource(R.drawable.button_bg_right);
                myAirDaysLine.setVisibility(View.VISIBLE);
                myAirMonthLine.setVisibility(View.GONE);
                break;
            case R.id.tt_month:
                ttMonth.setBackgroundResource(R.drawable.button_bg_right_press);
                ttDays.setBackgroundResource(R.drawable.button_bg_left);
                myAirMonthLine.setVisibility(View.VISIBLE);
                myAirDaysLine.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
