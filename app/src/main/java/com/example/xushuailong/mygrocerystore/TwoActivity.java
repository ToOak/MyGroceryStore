package com.example.xushuailong.mygrocerystore;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.xushuailong.mygrocerystore.adapter.TabAdapter;
import com.example.xushuailong.mygrocerystore.databinding.ActivityTwoBinding;

import com.example.xushuailong.mygrocerystore.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xushuailong on 2018/2/9.
 */

public class TwoActivity extends BaseActivity<ActivityTwoBinding> {

    private boolean finishing;
    private TextView et_bg;
    private TextView et_content;
    private ImageView back;
    private TextView tv_search;
    private FrameLayout fl;
    private TabLayout tabs;
    private ViewPager view_pager;
    private TabAdapter tabAdapter;
    private List<String> names = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_two;
    }


    @Override
    protected void initView() {
        et_bg = dataBinding.etBg;
        et_content = dataBinding.etContent;
        back = dataBinding.ivArrow;
        tv_search = dataBinding.searchBtn;
        fl = dataBinding.fl;
        tabs = dataBinding.tabs;
        view_pager = dataBinding.viewPager;


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

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "暂无此功能", Snackbar.LENGTH_SHORT)
                        .setAction("确定",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(TwoActivity.this, "click sure", Toast.LENGTH_SHORT).show();
                            }
                        })
//                        .setAction("取消", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(TwoActivity.this, "click cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        })
                        .setActionTextColor(TwoActivity.this.getResources().getColor(R.color.yellow))
                        .show();
            }
        });

//        tabs.addTab(tabs.newTab().setTag("tag1").setText("tab1"));
//        tabs.addTab(tabs.newTab().setTag("tag2").setText("tab2"));
//        tabs.addTab(tabs.newTab().setTag("tag3").setText("tab3"));
        names.add("tab0");
        names.add("tab1");
        names.add("tab2");
        tabAdapter = new TabAdapter(getSupportFragmentManager(),names);
        view_pager.setAdapter(tabAdapter);
        tabs.setupWithViewPager(view_pager);


    }

    @Override
    protected void loadData() {

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

}
