package com.example.xushuailong.mygrocerystore.base;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.utils.LogUtil;

/**
 * Created by xushuailong on 2018/2/2.
 */

public abstract class BaseActivity<B extends ViewDataBinding> extends Activity {

    /**
     * 通用初始化View布局
     */
    protected LayoutInflater mInflater;
    /**
     * 当前context对象
     */
    protected Context mContext;

    protected B dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("this is: " + this.getClass().getSimpleName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInflater = LayoutInflater.from(this);
        mContext = getApplicationContext();

        initIntent();
        initBindingLayout();
        initView();
        loadData();
    }

    private void initBindingLayout() {
        ViewGroup rootView = (ViewGroup) mInflater.inflate(R.layout.comment_layout,null);
        FrameLayout contentView = rootView.findViewById(R.id.comment_content);
        int contentResId = getLayoutId();
        if (contentResId != 0){
            dataBinding = DataBindingUtil.inflate(mInflater,contentResId,contentView,true);
        }

        setContentView(rootView);
//        ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
//        ViewGroup view = (ViewGroup) content;
//        contentView.addView(view);
    }

    /**
     * 初始化页面跳转的intent
     */
    protected void initIntent() {

    }

    /**
     * 提供给子类的布局文件入口
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 提供给子类中一些需要初始化的布局
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();
}
