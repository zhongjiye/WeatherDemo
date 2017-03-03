package com.demo.weather.cusview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.demo.weather.R;
import com.demo.weather.util.ColorEvaluator;

/**
 * 空气污染指数控件
 */
public class BoardView extends View {

    private Context context;

    private Paint linePaint, colorLinePaint, textPaint, desPaint;

    private float radius;//外部圆环的半径

    private int count = 6;

    private int lineDistance = 30;

    private int shortageAngle = 90;//缺失的部分的角度

    private int startAngle;//开始的角度

    private int sweepAngle;//扫过的角度

    private float screenWidth;//屏幕宽度

    private String startColor, endColor;

    private String[] numberTexts = {"0", "50", "100", "150", "200", "300", "500"};

    private String[] desCentreTexts = new String[7];
    private String[] desBottomTexts = new String[7];

    private int data = 0;

    private float edge = -1;


    public BoardView(Context context) {
        super(context);
        init(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setData(final int data) {
        this.data = data;
        invalidate();
    }


    private void init(Context context) {
        this.context = context;

        desCentreTexts[0] = context.getString(R.string.level_1);
        desCentreTexts[1] = context.getString(R.string.level_2);
        desCentreTexts[2] = context.getString(R.string.level_3);
        desCentreTexts[3] = context.getString(R.string.level_4);
        desCentreTexts[4] = context.getString(R.string.level_5);
        desCentreTexts[5] = context.getString(R.string.level_6);
        desCentreTexts[6] = context.getString(R.string.level_7);


        desBottomTexts[0] = context.getString(R.string.level_desc1);
        desBottomTexts[1] = context.getString(R.string.level_desc2);
        desBottomTexts[2] = context.getString(R.string.level_desc3);
        desBottomTexts[3] = context.getString(R.string.level_desc4);
        desBottomTexts[4] = context.getString(R.string.level_desc5);
        desBottomTexts[5] = context.getString(R.string.level_desc6);
        desBottomTexts[6] = context.getString(R.string.level_desc7);


        sweepAngle = 360 - shortageAngle;
        startAngle = 90 + shortageAngle / 2;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        radius = (screenWidth - 650) / 2;

        startColor = "#8AD00E";
        endColor = "#FF0000";

        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(2);
            linePaint.setAntiAlias(true);
            linePaint.setColor(Color.WHITE);
        }

        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(30);
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
            colorLinePaint.setStrokeCap(Paint.Cap.ROUND);
            colorLinePaint.setStrokeWidth(20);
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
            setMeasuredDimension((int) screenWidth, (int) (2 * radius + 150));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) screenWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (2 * radius + 150));
        }

        if (widthSpecMode == MeasureSpec.UNSPECIFIED && heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension((int) screenWidth, (int) (2 * radius + 150));
        } else if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension((int) screenWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, (int) (2 * radius + 150));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(screenWidth / 2, radius + 50);

        canvas.drawArc(new RectF(-radius, -radius, radius, radius), startAngle, sweepAngle, false, linePaint);

        drawInnerLine(canvas);

        drawOuterText(canvas);

        drawText(canvas);

        if (data != 0) {
            drawColorLine(canvas);
        }
    }


    /**
     * 绘制最里面的刻度
     *
     * @param canvas
     */
    private void drawInnerLine(Canvas canvas) {
        canvas.save();
        canvas.rotate(-45);
        for (int i = 0; i <= count; i++) {
            canvas.drawLine(-(radius - lineDistance), 0, -radius, 0, linePaint);
            canvas.rotate((float) 270 / 6);
        }
        canvas.restore();
    }

    /**
     * 绘制中间的渐变色刻度
     *
     * @param canvas
     */
    private void drawColorLine(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Path path = new Path();
        path.addArc(new RectF(-radius, -radius, radius, radius), startAngle, getDegree());
        PathMeasure outMeasure = new PathMeasure(path, false);
        float length = outMeasure.getLength();
        float[] startPos = new float[2];
        float[] endPos = new float[2];
        outMeasure.getPosTan(0, startPos, null);
        outMeasure.getPosTan(length, startPos, null);
        int currentColor = ColorEvaluator.evaluate(getDegree() / 270, startColor, endColor);
        colorLinePaint.setShader(new LinearGradient(startPos[0], startPos[1], endPos[0], endPos[1],
                Color.parseColor(startColor), currentColor, Shader.TileMode.CLAMP));
        canvas.drawArc(new RectF(-radius, -radius, radius, radius), startAngle + 2, getDegree() - 2, false, colorLinePaint);
    }


    /**
     * 绘制表盘外的刻度标识
     *
     * @param canvas
     */
    private void drawOuterText(Canvas canvas) {
        if (edge == -1) {
            edge = (float) (Math.sqrt(Math.pow((double) radius, 2) / 2));
        }
        canvas.drawText(numberTexts[1], -(radius + 40), 10, textPaint);
        canvas.drawText(numberTexts[5], (radius + 40), 10, textPaint);

        canvas.drawText(numberTexts[3], 0, -(radius + 20), textPaint);


        canvas.drawText(numberTexts[0], -(edge + 30), edge + 30, textPaint);
        canvas.drawText(numberTexts[6], edge + 30, edge + 30, textPaint);


        canvas.drawText(numberTexts[2], -(edge + 40), -edge, textPaint);
        canvas.drawText(numberTexts[4], edge + 40, -edge, textPaint);

    }


    /**
     * 绘制中间的数字和描述文字
     *
     * @param canvas
     */
    private void drawText(final Canvas canvas) {
        desPaint.setTextSize(150);
        canvas.drawText(String.valueOf(data), 0, 0, desPaint);
        desPaint.setTextSize(40);
        canvas.drawText(context.getString(R.string.air) + getDesText(), 0, 70, desPaint);
        desPaint.setTextSize(50);
        canvas.drawText(getBottomDes(), 0, radius + 35, desPaint);
    }


    /**
     * 获取对应空气指数的描述文字
     *
     * @return
     */
    private String getDesText() {
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
            return context.getString(R.string.unknown);
        }
    }

    private float getDegree() {
        if (data <= 0) {
            return 0;
        }
        if (data <= 200) {
            return (float) 180 * (data / (float) 200);
        } else if (data <= 300) {
            return 180 + 45 * ((data - 300) / (float) 100);
        } else if (data <= 500) {
            return 225 + ((data - 400) / (float) 200);
        }
        return 270;
    }

    /**
     * 获取对应空气指数的描述文字
     *
     * @return
     */
    private String getBottomDes() {
        if (data >= 0 && data < 25) {
            return desBottomTexts[0];
        } else if (data >= 25 && data < 75) {
            return desBottomTexts[1];
        } else if (data >= 75 && data < 125) {
            return desBottomTexts[2];
        } else if (data >= 125 & data < 175) {
            return desBottomTexts[3];
        } else if (data >= 175 && data < 250) {
            return desBottomTexts[4];
        } else if (data >= 250 && data < 400) {
            return desBottomTexts[5];
        } else if (data >= 400 && data <= 500) {
            return desBottomTexts[6];
        } else {
            return context.getString(R.string.unknown);
        }
    }


}

