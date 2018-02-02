package com.example.xushuailong.mygrocerystore.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.view.Window;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.utils.LogUtil;

/**
 * Created by xushuailong on 2018/2/2.
 */

public abstract class BaseActivity extends Activity {

    /**
     * 通用初始化View布局
     */
    protected LayoutInflater mInflater;
    /**
     * 当前context对象
     */
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("this is: " + this.getClass().getSimpleName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInflater = LayoutInflater.from(this);
        mContext = getApplicationContext();
        setContentView(R.layout.comment_layout);
        initIntent();
        initLayout();
        initView();
        loadData();
    }

    /**
     * 初始化页面跳转的intent
     */
    protected void initIntent() {

    }

    /**
     * 初始化content的布局，将子类中的布局加到主页面显示
     */
    private void initLayout() {
        ViewStub viewStub = findViewById(R.id.content_view_vs);
        viewStub.setLayoutResource(getlayoutId());
        viewStub.inflate();
    }

    /**
     * 提供给子类的布局文件入口
     *
     * @return
     */
    protected abstract int getlayoutId();

    /**
     * 提供给子类中一些需要初始化的布局
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();
}
