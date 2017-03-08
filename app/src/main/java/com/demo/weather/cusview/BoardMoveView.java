package com.demo.weather.cusview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.demo.weather.R;
import com.demo.weather.util.ColorEvaluator;
import com.demo.weather.util.WeatherUtil;

/**
 * 空气污染指数控件
 */
public class BoardMoveView extends View {

    private Context mContext;

    private Paint linePaint1, linePaint2, colorLinePaint, textPaint, desPaint;

    private float outerR;//外部圆环的半径

    private float innerR;//内部圆环的半径

    private int count = 60;//画count根线

    private int lineDistance = 50;

    private int shortageAngle = 90;//缺失的部分的角度

    private int startAngle;//开始的角度

    private int sweepAngle;//扫过的角度

    private float screenWidth;//屏幕宽度

    private long flushTime = 20;//绘制刷新间隔

    private String[] numberTexts = {"0", "50", "100", "150", "200", "300", "500"};

    private int now = 0;

    private int data = -1;

    private int dataCount = -1;

    private float edge = -1;

    private float perDegree;

    private float padding;

    private int startColor, endColor;


    public BoardMoveView(Context context) {
        super(context);
        init(context);
    }

    public BoardMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoardMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setData(final int data) {
        this.data = data;
        dataCount = -1;
        if (data < 150) {
            flushTime = 20;
        } else if (data < 300) {
            flushTime = 10;
        } else {
            flushTime = 5;
        }
        invalidate();
    }


    private void init(Context context) {
        mContext = context;

        sweepAngle = 360 - shortageAngle;
        startAngle = 90 + shortageAngle / 2;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        outerR = (screenWidth - 400) / 2;
        innerR = outerR - 100;
        padding = (outerR - innerR - lineDistance) / 2;
        perDegree = (float) 270 / count;


        startColor = context.getResources().getColor(R.color.colorAirLevel1);
        endColor = context.getResources().getColor(R.color.colorAirLevel7);

        if (linePaint1 == null) {
            linePaint1 = new Paint();
            linePaint1.setStyle(Paint.Style.STROKE);
            linePaint1.setStrokeWidth(2);
            linePaint1.setAntiAlias(true);
            linePaint1.setColor(Color.WHITE);
        }

        if (linePaint2 == null) {
            linePaint2 = new Paint();
            linePaint2.setStyle(Paint.Style.STROKE);
            linePaint2.setAntiAlias(true);
            linePaint2.setDither(true);
            linePaint2.setTextAlign(Paint.Align.CENTER);
            linePaint2.setStrokeWidth(15);
            linePaint2.setPathEffect(new CornerPathEffect(3));
            linePaint2.setColor(Color.WHITE);
        }

        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(40);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }

        if (desPaint == null) {
            desPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            desPaint.setAntiAlias(true);
            desPaint.setTextAlign(Paint.Align.CENTER);
            desPaint.setColor(Color.WHITE);
        }

        if (colorLinePaint == null) {
            colorLinePaint = new Paint();
            colorLinePaint.setStyle(Paint.Style.STROKE);
            colorLinePaint.setAntiAlias(true);
            colorLinePaint.setDither(true);
            colorLinePaint.setStrokeWidth(15);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) screenWidth, (int) (2 * outerR + 200));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) screenWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (2 * outerR + 200));
        }

        if (widthSpecMode == MeasureSpec.UNSPECIFIED && heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension((int) screenWidth, (int) (2 * outerR + 200));
        } else if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension((int) screenWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, (int) (2 * outerR + 200));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(screenWidth / 2, outerR + 150);

        canvas.drawArc(new RectF(-outerR, -outerR, outerR, outerR), startAngle, sweepAngle, false, linePaint1);

        canvas.drawArc(new RectF(-innerR, -innerR, innerR, innerR), startAngle, sweepAngle, false, linePaint1);

        drawLine(canvas);

        drawInnerLine(canvas);

        drawColorLine(canvas);

        drawOuterText(canvas);

        drawText(canvas);
    }

    /**
     * 绘制中间刻度的底色
     */
    private void drawLine(Canvas canvas) {
        canvas.rotate(-45);
        float startX = -innerR - padding;
        float endX = -outerR + padding;
        for (float degreeCount = 0; degreeCount <= 270; degreeCount = degreeCount + perDegree) {
            canvas.drawLine(startX, 0, endX, 0, linePaint2);
            canvas.rotate(perDegree);
        }
        canvas.rotate(90 - perDegree);
    }

    /**
     * 绘制最里面的刻度
     */
    private void drawInnerLine(Canvas canvas) {
        for (int i = 0; i <= count; i = i + 10) {
            canvas.drawLine(-innerR, 0, -innerR + padding, 0, linePaint1);
            canvas.rotate(perDegree * 10);
        }
        canvas.rotate(45);
    }

    /**
     * 绘制中间的渐变色刻度
     */
    private void drawColorLine(Canvas canvas) {
        float counts = 0;

        float progress = ((float) now / data) * (getDataCount() * perDegree);

        for (float degreeCount = 0; degreeCount <= progress; degreeCount = degreeCount + perDegree) {
            colorLinePaint.setColor(getColor(degreeCount / (float) 270));
            canvas.drawLine(-innerR - padding, 0, -outerR + padding, 0, colorLinePaint);
            canvas.rotate(perDegree);
            counts = degreeCount;
        }

        canvas.rotate(-counts + 45 - perDegree);
    }

    private int getColor(float fraction) {
        return ColorEvaluator.evaluate(fraction, startColor, endColor);
    }


    /**
     * 绘制表盘外的刻度标识
     */
    private void drawOuterText(Canvas canvas) {
        if (edge == -1) {
            edge = (float) (Math.sqrt(Math.pow((double) outerR, 2) / 2));
        }
        canvas.drawText(numberTexts[1], -(outerR + 60), -20, textPaint);
        canvas.drawText(mContext.getString(R.string.level_2), -(outerR + 60), 20, textPaint);

        canvas.drawText(numberTexts[3], 0, -(outerR + 60), textPaint);
        canvas.drawText(mContext.getString(R.string.level_4), 0, -(outerR + 20), textPaint);

        canvas.drawText(numberTexts[5], (outerR + 60), -20, textPaint);
        canvas.drawText(mContext.getString(R.string.level_6), (outerR + 60), 20, textPaint);


        canvas.drawText(numberTexts[0], -(edge + 60), edge, textPaint);
        canvas.drawText(mContext.getString(R.string.level_1), -(edge + 60), edge + 40, textPaint);

        canvas.drawText(numberTexts[6], edge + 60, edge, textPaint);
        canvas.drawText(mContext.getString(R.string.level_7), edge + 60, edge + 40, textPaint);

        canvas.drawText(numberTexts[2], -(edge + 60), -edge, textPaint);
        canvas.drawText(mContext.getString(R.string.level_3), -(edge + 60), -(edge + 40), textPaint);

        canvas.drawText(numberTexts[4], edge + 60, -edge, textPaint);
        canvas.drawText(mContext.getString(R.string.level_5), edge + 60, -(edge + 40), textPaint);
    }


    /**
     * 绘制中间的数字和描述文字
     */
    private void drawText(final Canvas canvas) {
        desPaint.setTextSize(180);
        canvas.drawText(String.valueOf(now), 0, 0, desPaint);
        desPaint.setTextSize(60);
        canvas.drawText(mContext.getString(R.string.air) + WeatherUtil.getDes(mContext, data, 0),
            0, 70, desPaint);
        desPaint.setTextSize(50);
        canvas.drawText(WeatherUtil.getDes(mContext, data, 1), 0, outerR + 35, desPaint);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (data != now) {
                    if (data - now < 5) {
                        now = data;
                    } else {
                        now = now + 5;
                    }
                    if (now <= data) {
                        invalidate();
                    }
                }
            }
        }, flushTime);
    }

    /**
     * 计算需要重新绘制颜色的刻度数量
     */
    private int getDataCount() {
        if (dataCount != -1) {
            return dataCount;
        }
        if (data <= 200) {
            return dataCount = Math.round(data / (float) 5);
        } else if (data <= 300) {
            return dataCount = 40 + Math.round((data - 200) / (float) 10);
        } else {
            return dataCount = 50 + Math.round((data - 300) / (float) 20);
        }
    }

}