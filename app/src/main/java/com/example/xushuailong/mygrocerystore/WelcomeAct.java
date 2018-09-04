package com.example.xushuailong.mygrocerystore;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.animation.SwingAnimation;

public class WelcomeAct extends AppCompatActivity {
    private TextView tvTimecount;
    private ImageView imgAdvertise;
    private int adTime = 6000;//倒计时秒数
    private int timeInterval = 1000;//倒计时间隔
    private CountDownTimer mTimer;//计时器
    private int change = 0;//记录下标
    private int[] ids = new int[]{R.drawable.android0, R.drawable.android1, R.drawable.android2};
    private Drawable[] drawables;//图片集合
    private Thread mThread;//线程
    private boolean mThreadFlag = true;//线程结束标志符
    private ImageView rockImg;
    private Button playBtn;
    private SwingAnimation swingAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_welcome);
        initView();
        initData();
        initEvent();
    }

    //定义hander
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int duration = msg.arg1;
            TransitionDrawable transitionDrawable = new TransitionDrawable(
                    new Drawable[]{
                            drawables[change % ids.length],
                            drawables[(change + 1) % ids.length]
                    }
            );
            change++;//改变标识位置
            imgAdvertise.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(duration);
            return false;
        }
    });

    //开启线程发送消息，让transition一直在改变
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            //这个while(true)是做死循环
            while (mThreadFlag) {
                int duration = 1000;//改变的间隔
                Message message = mHandler.obtainMessage();
                message.arg1 = duration;
                mHandler.sendMessage(message);
                try {
                    Thread.sleep(duration);
                    //隔duration秒发送一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initView() {
        tvTimecount = findViewById(R.id.tv_advert);
        imgAdvertise = findViewById(R.id.iv_advetise);
        rockImg = findViewById(R.id.rock);
        playBtn = findViewById(R.id.play);
        //填充图片
        drawables = new Drawable[ids.length];
        for (int i = 0; i < ids.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawables[i] = getDrawable(ids[i]);
            } else {
                drawables[i] = getResources().getDrawable(ids[i]);
            }
        }
    }

    private void initData() {
        // 初始化计时器,第一个参数是共要倒计时的秒数，第二个参数是倒计时的间隔
        mTimer = new CountDownTimer(adTime, timeInterval) {
            // 倒计时开始时要做的事情，参数m是直到完成的时间
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimecount.setText("" + millisUntilFinished / 1000 + "s跳过广告");
            }

            // 结束计时后要做的工作
            @Override
            public void onFinish() {
                jumpActivity();
            }
        };
        //开启计时器
        mTimer.start();
        //开启线程，改变transition,切换图片
        mThread = new Thread(new MyRunnable());
        mThread.start();
        swingAnimation = new SwingAnimation(
                0f, 60f, -60f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        swingAnimation.setDuration(4000);
        swingAnimation.setRepeatCount(Animation.INFINITE);
//        swingAnimation.setStartOffset(500);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rockImg.startAnimation(swingAnimation);
            }
        });

    }

    private void initEvent() {
        tvTimecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.cancel();
                jumpActivity();
            }
        });
    }

    // 跳转页面
    private void jumpActivity() {
        //如果还没结束当前的页面，就结束
        if (!isFinishing()) {
            finish();
        }
        Intent it = new Intent(WelcomeAct.this, MainActivity.class);
        startActivity(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mThread.stop(); 不推荐使用
        mThreadFlag = false;//结束线程

    }
}
