package com.example.xushuailong.mygrocerystore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xushuailong.mygrocerystore.base.BaseActivity;
import com.example.xushuailong.mygrocerystore.utils.LogUtil;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private TextView tv;

    @Override
    protected int getlayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv = findViewById(R.id.tv_main);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwoActivity.class);
                //获取控件在屏幕中的坐标
                int location[] = new int[2];
                tv.getLocationOnScreen(location);
                LogUtil.e("tv location on screen: " + "\t" + location.length + location[0] + "\t" + location[1]);
                intent.putExtra("x", location[0]);
                intent.putExtra("y", location[1]);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void loadData() {
//        https://gank.io/post/560e15be2dca930e00da1083

        String[] names = {"android", "ios", "h5"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtil.e("names: " + s);
//                        throw new RuntimeException("test rx observable throwable!");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e("throwable: " + throwable.getMessage());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        LogUtil.e("complete");
                    }
                });

        final int drawableRes = R.drawable.ic_launcher_background;
        final ImageView imageView = findViewById(R.id.img);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("on complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("on error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }
                });

        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        LogUtil.e("number:" + number);
                    }
                });

        Observable.just("images/logo.png") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        return getBitmapFromPath(filePath); // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        showBitmap(bitmap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e("throwable: " + throwable.getMessage());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        LogUtil.e("map complete");
                    }
                });
    }

    private void showBitmap(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.img1);
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromPath(String filePath) {
        return drawableToBitmap(getResources().getDrawable(R.drawable.ic_launcher_background));
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
