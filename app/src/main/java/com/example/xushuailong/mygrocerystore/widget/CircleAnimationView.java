package com.example.xushuailong.mygrocerystore.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.*;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class CircleAnimationView extends RelativeLayout {
    private final static String TAG = "CicleAnimation";
    private RectF mRect = new RectF(10, 10, 160, 160);
    private int mBeginAngle = 0;
    private int mEndAngle = 360;
    private int mFrontColor = 0xff00ff00;
    private float mFrontLine = 10;
    private Style mFrontStyle = Style.FILL;
    private int mShadeColor = 0xffeeeeee;
    private float mShadeLine = 10;
    private Style mShadeStyle = Style.STROKE;
    private ShadeView mShadeView;
    private FrontView mFrontView;
    private int mRate = 2;
    private int mDrawTimes;
    private int mInterval = 70;
    private int mFactor;
    private Context mContext;
    private int mSeq = 0;
    private int mDrawingAngle = 0;

    public CircleAnimationView(Context context) {
        super(context);
        mContext = context;
    }

    public CircleAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CircleAnimationView render() {
        mShadeView = new ShadeView(mContext);
        this.addView(mShadeView);
        mFrontView = new FrontView(mContext);
        this.addView(mFrontView);
        refresh();
        return this;
    }

    public void refresh() {
        mSeq = 0;
        mDrawingAngle = 0;
        mDrawTimes = mEndAngle / mRate;
        mFactor = mDrawTimes / mInterval + 1;
        Log.d(TAG, "mDrawTimes=" + mDrawTimes + ",mInterval=" + mInterval + ",mFactor=" + mFactor);
        mFrontView.invalidateView();
    }

    public void setRect(int left, int top, int right, int bottom) {
        mRect = new RectF(left, top, right, bottom);
    }

    public void setAngle(int begin_angle, int end_angle) {
        mBeginAngle = begin_angle;
        mEndAngle = end_angle;
    }

    //speed:每次移动几个度数   frames:每秒移动几帧
    public void setmRate(int speed, int frames) {
        mRate = speed;
        mInterval = 1000 / frames;
    }

    public void setFront(int color, float line, Style style) {
        mFrontColor = color;
        mFrontLine = line;
        mFrontStyle = style;
    }

    public void setShade(int color, float line, Style style) {
        mShadeColor = color;
        mShadeLine = line;
        mShadeStyle = style;
    }

    class ShadeView extends View {
        Paint paint;

        public ShadeView(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(mShadeColor);
            paint.setStrokeWidth(mShadeLine);
            paint.setStyle(mShadeStyle);
        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawArc(mRect, mBeginAngle, 360, false, paint);
        }
    }

    class FrontView extends View {
        Paint paint;

        public FrontView(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);         //设置画笔为无锯齿
            paint.setDither(true);            //防抖动
            paint.setColor(mFrontColor);       //设置画笔颜色
            paint.setStrokeWidth(mFrontLine);  //线宽
            paint.setStyle(mFrontStyle);       //画笔类型 STROKE空心 FILL 实心
            //paint.setStrokeJoin(Paint.Join.ROUND); //画笔接洽点类型 如影响矩形直角的外轮廓
            paint.setStrokeCap(Paint.Cap.ROUND);  ////画笔笔刷类型 如影响画笔的始末端
        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint1 = new Paint();
            paint1.setColor(Color.parseColor("#35ff0000"));
            mRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRect(mRect, paint1);
            canvas.drawArc(mRect, mBeginAngle, (float) (mDrawingAngle), true, paint);
        }

        public void invalidateView() {
            handler.postDelayed(drawRunnable, 0);
        }

        private Handler handler = new Handler();

        Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                if (mDrawingAngle >= mEndAngle) {
                    mDrawingAngle = mEndAngle;
                    invalidate();
                    //移除当前Runnable
                    handler.removeCallbacks(drawRunnable);
                } else {
                    mDrawingAngle = mSeq * mRate;
                    mSeq++;
                    handler.postDelayed(drawRunnable, (long) (mInterval - mSeq / mFactor));
                    invalidate();
                }
            }
        };
    }

}
