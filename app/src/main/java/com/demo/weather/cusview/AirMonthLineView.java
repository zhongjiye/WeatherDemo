package com.demo.weather.cusview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.demo.weather.R;
import com.demo.weather.bean.MonthAir;
import com.demo.weather.util.ColorEvaluator;
import com.demo.weather.util.DateUtil;
import com.demo.weather.util.WeatherUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 15天空气指数自定义控件
 */
public class AirMonthLineView extends View {
    private Context context;
    private int height;
    private int degree;
    private int count;
    private int degreeLineHeight;
    private int paddingBottom;
    private int paddingTop;
    private int xLength;
    private int xAxisY;
    private float perDegree;
    private int startColor, endColor;

    private Paint linePaint, degreeTextPaint, thinLinePaint, brokeLinePaint, dashPaint;
    private Path path;

    /**
     * 即将要穿越的点集合
     */
    private List<Point> points = new ArrayList<>();
    /**
     * 中点集合
     */
    private List<Point> midPoints = new ArrayList<>();
    /**
     * 中点的中点集合
     */
    private List<Point> midMidPoints = new ArrayList<>();
    /**
     * 移动后的点集合(控制点)
     */
    private List<Point> controlPoints = new ArrayList<>();

    private String[] desCentreTexts = new String[7];

    private ArrayList<MonthAir> datas;

    public AirMonthLineView(Context context) {
        super(context);
        init(context);
    }

    public AirMonthLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AirMonthLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setData(ArrayList<MonthAir> datas) {
        this.datas = datas;
        invalidate();
    }


    private void init(Context context) {
        this.context = context;
        height = 650;
        degree = 135;
        count = 16;
        degreeLineHeight = 10;
        paddingBottom = 100;
        paddingTop = 150;
        xLength = height - paddingBottom - paddingTop;
        xAxisY = height - paddingBottom;
        perDegree = (float) xLength / 500;

        startColor = context.getResources().getColor(R.color.colorAirLevel1);
        endColor = context.getResources().getColor(R.color.colorAirLevel7);

        path = new Path();

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(2);
        linePaint.setAntiAlias(true);

        dashPaint = new Paint();
        dashPaint.setColor(Color.WHITE);
        dashPaint.setStrokeWidth(2);
        dashPaint.setAntiAlias(true);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{6, 4}, 1));

        degreeTextPaint = new Paint();
        degreeTextPaint.setColor(Color.WHITE);
        degreeTextPaint.setStrokeWidth(1);
        degreeTextPaint.setAntiAlias(true);
        degreeTextPaint.setTextSize(30);
        degreeTextPaint.setTextAlign(Paint.Align.CENTER);

        thinLinePaint = new Paint();
        thinLinePaint.setColor(Color.WHITE);
        thinLinePaint.setAlpha(55);
        thinLinePaint.setStrokeWidth(2);
        thinLinePaint.setAntiAlias(true);

        brokeLinePaint = new Paint();
        brokeLinePaint.setColor(Color.WHITE);
        brokeLinePaint.setStrokeWidth(2);
        brokeLinePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(degree * count, height);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(degree * count, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, height);
        }

        if (widthSpecMode == MeasureSpec.UNSPECIFIED && heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(degree * count, height);
        } else if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(degree * count, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, height);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAxis(canvas);
        drawSplitLine(canvas);
        drawDegreeLine(canvas);
        drawMonthDesText(canvas);
        if (datas != null && datas.size() != 0) {
            initPoint();
            drawBezier(canvas);
            drawPoint(canvas);
            drawPointText(canvas);
        }
    }


    /**
     * 绘制X轴
     */
    private void drawAxis(Canvas canvas) {
        canvas.drawLine(0, xAxisY, count * degree, xAxisY, linePaint);//最上方分割线
        canvas.drawLine(0, paddingTop, count * degree, paddingTop, linePaint);//X轴
    }

    /**
     * 绘制平行于X轴的分割线
     */
    private void drawSplitLine(Canvas canvas) {
        float perDegree = xLength / (float) 5;
        for (int i = 1; i <= 5; i++) {
            canvas.drawLine(0, paddingTop + perDegree * i, count * degree, paddingTop + perDegree * i, thinLinePaint);
        }
    }

    /**
     * 绘制刻度
     */
    private void drawDegreeLine(Canvas canvas) {
        for (int i = 1; i <= count; i++) {
            canvas.drawLine(i * degree, xAxisY, i * degree, xAxisY - degreeLineHeight, linePaint);
        }
    }

    /**
     * 绘制刻度线下方日期标识和最上方月份标识
     */
    private void drawMonthDesText(Canvas canvas) {
        int lastMonth = DateUtil.getDateMonth(datas.get(0).getDay());
        canvas.drawText(lastMonth + "月", degree, paddingTop - 15, degreeTextPaint);
        for (int i = 1; i < count; i++) {
            if (i == 1) {
                canvas.drawText("今日", i * degree, xAxisY + 40, degreeTextPaint);
            } else {
                if (datas != null) {
                    int currentMonth = DateUtil.getDateMonth(datas.get(i - 1).getDay());
                    if (currentMonth != lastMonth) {
                        canvas.drawText(currentMonth + "月", i * degree, paddingTop - 15, degreeTextPaint);
                        lastMonth = currentMonth;
                    }
                    canvas.drawText(DateUtil.getDate(datas.get(i - 1).getDay()), i * degree, xAxisY + 40,
                        degreeTextPaint);
                }
            }
        }
    }

    private void initPoint() {
        points.clear();
        midPoints.clear();
        midMidPoints.clear();
        controlPoints.clear();
        for (int i = 0; i < datas.size(); i++) {
            points.add(new Point(degree * (i + 1), (int) (xAxisY - datas.get(i).getAirNum() * perDegree)));
        }
        for (int i = 0; i < points.size() - 1; i++) {
            midPoints.add(new Point((points.get(i).x + points.get(i + 1).x) / 2, (points.get(i).y + points.get(i + 1)
                .y) / 2));
        }

        for (int i = 0; i < midPoints.size() - 1; i++) {
            midMidPoints.add(new Point((midPoints.get(i).x + midPoints.get(i + 1).x) / 2, (midPoints.get(i).y +
                midPoints.get(i + 1).y) / 2));

        }

        for (int i = 0; i < points.size(); i++) {
            if (i == 0 || i == points.size() - 1) {
                continue;
            } else {
                Point before = new Point();
                Point after = new Point();
                before.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i - 1).x;
                before.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i - 1).y;
                after.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i).x;
                after.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i).y;
                controlPoints.add(before);
                controlPoints.add(after);
            }
        }
    }

    /**
     * 绘制数据点
     */
    private void drawPoint(Canvas canvas) {
        path.reset();
        for (int i = 0; i < points.size(); i++) {
            Paint paint = getPointPaint(i);
            if (i == 0) {
                path.moveTo(points.get(i).x, xAxisY);
                path.lineTo(points.get(i).x, points.get(i).y);
                canvas.drawPath(path, dashPaint);

                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(points.get(i).x, points.get(i).y, 10, paint);//实心圆点

                paint.setAlpha(100);
                canvas.drawCircle(points.get(i).x, points.get(i).y, 20, paint);//大实心圆点
                paint.setAlpha(255);
            } else {
                canvas.drawCircle(points.get(i).x, points.get(i).y, 10, paint);//实心圆点
            }
        }
    }

    /**
     * 绘制数据点上的文字
     */
    private void drawPointText(Canvas canvas) {
        for (int i = 0; i < points.size(); i++) {
            canvas.drawText(WeatherUtil.getDes(context, datas.get(i).getAirNum(), 0), points.get(i)
                    .x,
                points.get(i)
                    .y - 30,
                degreeTextPaint);
        }
    }

    /**
     * 绘制两点之间的贝塞尔曲线
     */
    private void drawBezier(Canvas canvas) {
        path.reset();
        for (int i = 0; i < points.size(); i++) {
            path.reset();
            if (i == 0) {// 第一条为二阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);// 起点
                path.quadTo(controlPoints.get(i).x, controlPoints.get(i).y,// 控制点
                    points.get(i + 1).x, points.get(i + 1).y);
                canvas.drawPath(path, getBrokeLinePaint(i));
            } else if (i < points.size() - 2) {// 三阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);
                path.cubicTo(controlPoints.get(2 * i - 1).x, controlPoints.get(2 * i - 1).y,// 控制点
                    controlPoints.get(2 * i).x, controlPoints.get(2 * i).y,// 控制点
                    points.get(i + 1).x, points.get(i + 1).y);// 终点
                canvas.drawPath(path, getBrokeLinePaint(i));
            } else if (i == points.size() - 2) {// 最后一条为二阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);// 起点
                path.quadTo(controlPoints.get(controlPoints.size() - 1).x, controlPoints.get(controlPoints.size() -
                    1).y, points.get(i + 1).x, points.get(i + 1).y);// 终点
                canvas.drawPath(path, getBrokeLinePaint(i));
            }
        }
    }


    private Paint getBrokeLinePaint(int index) {
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(getLinearGradient(index));
        return paint;
    }

    private Paint getPointPaint(int index) {
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getCurrentColor(index));
        return paint;
    }

    private LinearGradient getLinearGradient(int index) {
        if (index < datas.size() - 1) {
            return new LinearGradient(points.get(index).x, points.get(index).y,//渐变起始点坐标
                points.get(index + 1).x, points.get(index + 1).y,//渐变终点坐标
                getCurrentColor(index), getCurrentColor(index + 1),//渐变开始和结束颜色
                Shader.TileMode.REPEAT);
        }
        return null;
    }

    private int getCurrentColor(int index) {
        float fraction = getFraction(datas.get(index).getAirNum());
        return ColorEvaluator.evaluate(fraction, startColor, endColor);
    }

    private float getFraction(int data) {
        if (data < 0) {
            return 0;
        } else if (data < 200) {
            return data / (float) 500;
        } else if (data <= 300) {
            return 0.4f + ((data - 200) / (float) 100) * 0.2f;
        } else if (data <= 500) {
            return 0.6f + ((data - 300) / (float) 200) * 0.4f;
        }
        return 1.0f;
    }

}
