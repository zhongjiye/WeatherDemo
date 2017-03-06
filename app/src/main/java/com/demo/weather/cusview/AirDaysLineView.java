package com.demo.weather.cusview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.weather.R;
import com.demo.weather.bean.DaysAir;
import com.demo.weather.util.ColorEvaluator;
import com.demo.weather.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 48小时空气指数自定义控件
 */
public class AirDaysLineView extends View {

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
    private Paint linePaint, degreeTextPaint, brokeLinePaint;
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

    private ArrayList<DaysAir> datas;

    public AirDaysLineView(Context context) {
        super(context);
        init(context);
    }

    public AirDaysLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AirDaysLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setData(ArrayList<DaysAir> datas) {
        this.datas = datas;
        invalidate();
    }


    private void init(Context context) {
        height = 650;
        degree = 135;
        count = 49;
        degreeLineHeight = 10;
        paddingBottom = 100;
        paddingTop = 150;
        xLength = height - paddingBottom - paddingTop;
        xAxisY = height - paddingBottom;
        perDegree = (float) xLength / 500;

        desCentreTexts[0] = context.getString(R.string.level_1);
        desCentreTexts[1] = context.getString(R.string.level_2);
        desCentreTexts[2] = context.getString(R.string.level_3);
        desCentreTexts[3] = context.getString(R.string.level_4);
        desCentreTexts[4] = context.getString(R.string.level_5);
        desCentreTexts[5] = context.getString(R.string.level_6);
        desCentreTexts[6] = context.getString(R.string.level_7);

        startColor = Color.parseColor("#8AD00E");
        endColor = Color.parseColor("#FF0000");

        path = new Path();

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(2);
        linePaint.setAntiAlias(true);

        brokeLinePaint = new Paint();
        brokeLinePaint.setColor(Color.WHITE);
        brokeLinePaint.setStrokeWidth(5);
        brokeLinePaint.setAntiAlias(true);

        degreeTextPaint = new Paint();
        degreeTextPaint.setColor(Color.WHITE);
        degreeTextPaint.setStrokeWidth(1);
        degreeTextPaint.setAntiAlias(true);
        degreeTextPaint.setTextSize(30);
        degreeTextPaint.setTextAlign(Paint.Align.CENTER);
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
        initPoint();
        drawBezier(canvas);
        drawPoint(canvas);
        drawDegreeText(canvas);
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
        linePaint.setAlpha(55);
        float perDegree = xLength / (float) 5;
        for (int i = 1; i <= 5; i++) {
            canvas.drawLine(0, paddingTop + perDegree * i, count * degree, paddingTop + perDegree * i, linePaint);
        }
    }

    /**
     * 绘制刻度
     */
    private void drawDegreeLine(Canvas canvas) {
        for (int i = 1; i < count; i++) {
            canvas.drawLine(i * degree, xAxisY, i * degree, xAxisY - degreeLineHeight, linePaint);
        }
    }


    /**
     * 绘制刻度文字表述
     */
    private void drawDegreeText(Canvas canvas) {
        String lastDay = DateUtil.getDate(new Date(), "MM月dd日");
        canvas.drawText(lastDay, degree, paddingTop - 15, degreeTextPaint);
        int day = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (i == 0) {
                canvas.drawText("当前", degree, xAxisY + 40, degreeTextPaint);
            } else {
                if (datas.get(i).getClock() == 0) {
                    day++;
                    canvas.drawText(DateUtil.getSomeDay(new Date(), day, "MM月dd日"), (i + 1) * degree, paddingTop -
                        15, degreeTextPaint);
                }
                canvas.drawText(datas.get(i).getClock() + ":00", (i + 1) * degree, xAxisY + 40, degreeTextPaint);
            }
        }
    }

    private void initPoint() {
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

    private LinearGradient getLinearGradient() {
        int sc = ColorEvaluator.evaluate(datas.get(0).getAirNum() / (float) 500,
            "#8AD00E", "#FF0000");
        int ec = ColorEvaluator.evaluate(datas.get(datas.size() - 1).getAirNum() / (float) 500,
            "#8AD00E", "#FF0000");
        return new LinearGradient(points.get(0).x, points.get(0).y, points.get(points.size() - 1).x, points.get
            (points.size() - 1).y, sc, ec, Shader.TileMode.CLAMP);
    }

    private LinearGradient getLinearGradient(int index) {
        int sc = ColorEvaluator.evaluate(datas.get(index).getAirNum() / (float) 500,
            "#8AD00E", "#FF0000");
        int ec = ColorEvaluator.evaluate(datas.get(index + 1).getAirNum() / (float) 500,
            "#8AD00E", "#FF0000");
        return new LinearGradient(points.get(index).x, points.get(index).y, points.get(index + 1).x,
            points.get
                (index + 1).y, sc, ec, Shader.TileMode.CLAMP);
    }


    /**
     * 绘制两点之间的贝塞尔曲线
     */
    private void drawBezier(Canvas canvas) {
        brokeLinePaint.setShader(getLinearGradient());
        brokeLinePaint.setStrokeWidth(2);
        brokeLinePaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < points.size(); i++) {
            path.reset();
            if (i == 0) {// 第一条为二阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);// 起点
                path.quadTo(controlPoints.get(i).x, controlPoints.get(i).y,// 控制点
                    points.get(i + 1).x, points.get(i + 1).y);
                canvas.drawPath(path, brokeLinePaint);
            } else if (i < points.size() - 2) {// 三阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);
                path.cubicTo(controlPoints.get(2 * i - 1).x, controlPoints.get(2 * i - 1).y,// 控制点
                    controlPoints.get(2 * i).x, controlPoints.get(2 * i).y,// 控制点
                    points.get(i + 1).x, points.get(i + 1).y);// 终点
                canvas.drawPath(path, brokeLinePaint);
            } else if (i == points.size() - 2) {// 最后一条为二阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);// 起点
                path.quadTo(controlPoints.get(controlPoints.size() - 1).x, controlPoints.get(controlPoints.size() -
                    1).y, points.get(i + 1).x, points.get(i + 1).y);// 终点
                canvas.drawPath(path, brokeLinePaint);
            }
        }
    }

    /**
     * 绘制数据点
     */
    private void drawPoint(Canvas canvas) {
        path.reset();
        brokeLinePaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < points.size(); i++) {
            canvas.drawCircle(points.get(i).x, points.get(i).y, 10, brokeLinePaint);//实心圆点
        }
    }


    public int getExtra() {
        return degree;
    }

    public int[] getTextHeight(int x) {
        int count = x / degree;
        int residue = x % degree;
        int[] nums = new int[2];
        if (residue == 0) {//刚好在刻度点
            nums[0] = (int) (xAxisY - datas.get(count).getAirNum() * perDegree);
            nums[1] = datas.get(count).getAirNum();
        } else {
            if (count >= datas.size() - 1) {
                nums[0] = (int) (xAxisY - datas.get(datas.size() - 1).getAirNum() * perDegree);
                nums[1] = datas.get(datas.size() - 1).getAirNum();
            } else {//根据斜率计算相应点的坐标
                float x1 = count * degree, y1 = xAxisY - datas.get(count).getAirNum() * perDegree, x2 = x1 + degree,
                    y2 = xAxisY - datas.get(count + 1).getAirNum() * perDegree;
                float xieLv = (y2 - y1) / (x2 - x1);
                nums[0] = (int) (xieLv * (x - x1) + y1);
                nums[1] = (int) ((xAxisY - nums[0]) / perDegree);
            }
        }
        return nums;
    }


    public String getDesText(int data) {
        if (data >= 0 && data < 25) {
            return desCentreTexts[0];
        } else if (data >= 25 && data < 75) {
            return desCentreTexts[1];
        } else if (data >= 75 && data < 125) {
            return desCentreTexts[2];
        } else if (data >= 125 & data < 175) {
            return desCentreTexts[3];
        } else if (data >= 175 && data < 250) {
            return desCentreTexts[4];
        } else if (data >= 250 && data < 400) {
            return desCentreTexts[5];
        } else if (data >= 400 && data <= 500) {
            return desCentreTexts[6];
        } else {
            return "";
        }
    }
}
