package com.example.xushuailong.mygrocerystore;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.base.BaseActivity;

/**
 * Created by xushuailong on 2018/2/9.
 */

public class TwoActivity extends BaseActivity {

    private TextView et_bg;
    private TextView et_content;
    private ImageView back;
    private TextView tv_search;
    private FrameLayout fl;
    private boolean finishing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        et_bg =  findViewById(R.id.et_bg);
        et_content =  findViewById(R.id.et_content);
        fl =  findViewById(R.id.fl);
        back =  findViewById(R.id.iv_arrow);
        tv_search = findViewById(R.id.search_btn);

        et_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finishing) {
                    finishing = true;
                    outAnimation();
                }
            }
        });
        //监听布局是否发生变化
        et_bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                et_bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                inAnimation();
            }
        });

    }

    private void inAnimation() {
        float originY = getIntent().getIntExtra("y", 0);
        //获取到搜索框在TwoActivity界面的位置
        int[] location = new int[2];
        et_bg.getLocationOnScreen(location);
        //计算位置的差值
        final float translateY = originY - (float) location[1];
        //将第一个界面的位置设置给搜索框
        et_bg.setY(et_bg.getY() + translateY);
        //同步设置搜索框中的文字
        et_content.setY(et_bg.getY() + (et_bg.getHeight() - et_content.getHeight()) / 2);
        float top = getResources().getDisplayMetrics().density * 20;
        //ValueAnimator是一个很厉害的东西，你只需要给他初始值和结束值，他会自动计算中间的过度
        final ValueAnimator translateVa = ValueAnimator.ofFloat(et_bg.getY(), top);
        //这个是由下移动到上面的监听
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                et_bg.setY((Float) valueAnimator.getAnimatedValue());
                et_content.setY(et_bg.getY() + (et_bg.getHeight() - et_content.getHeight()) / 2);
                back.setY(et_bg.getY() + (et_bg.getHeight() - back.getHeight()) / 2);
                tv_search.setY(et_bg.getY() + (et_bg.getHeight() - tv_search.getHeight()) / 2);
            }
        });
        //这个是缩小搜索框的监听
        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                et_bg.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        //这个是设置透明度
        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                back.setAlpha((Float) valueAnimator.getAnimatedValue());
                tv_search.setAlpha((Float) valueAnimator.getAnimatedValue());
                fl.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaVa.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        translateVa.start();
        scaleVa.start();
    }

    private void outAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int[] location = new int[2];
        et_bg.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];
        et_bg.setY(et_bg.getY() + translateY);
        et_content.setY(et_bg.getY() + (et_bg.getHeight() - et_content.getHeight()) / 2);
        float top = getResources().getDisplayMetrics().density * 20;
        final ValueAnimator translateVa = ValueAnimator.ofFloat(top, et_bg.getY());

        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                et_bg.setY((Float) valueAnimator.getAnimatedValue());
                et_content.setY(et_bg.getY() + (et_bg.getHeight() - et_content.getHeight()) / 2);
                back.setY(et_bg.getY() + (et_bg.getHeight() - back.getHeight()) / 2);
                tv_search.setY(et_bg.getY() + (et_bg.getHeight() - tv_search.getHeight()) / 2);
            }
        });

        translateVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                et_bg.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(1f, 0);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                back.setAlpha((Float) valueAnimator.getAnimatedValue());
                tv_search.setAlpha((Float) valueAnimator.getAnimatedValue());
                fl.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaVa.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        translateVa.start();
        scaleVa.start();
    }

    @Override
    public void onBackPressed() {
        if (!finishing) {
            finishing = true;
            outAnimation();
        }
    }

    @Override
    protected int getlayoutId() {
        return R.layout.activity_two;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }
}
