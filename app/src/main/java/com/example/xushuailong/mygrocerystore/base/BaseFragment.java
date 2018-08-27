package com.example.xushuailong.mygrocerystore.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    protected Context context;
    protected LayoutInflater inflater;
    protected View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);
            this.inflater = LayoutInflater.from(context);
            initView(rootView);
            loadData();
        }
        ViewGroup parentView = (ViewGroup) rootView.getParent();
        if (parentView != null) {
            parentView.removeView(rootView);
        }
        return rootView;
    }

    protected abstract void loadData();

    protected abstract void initView(View rootView);

    protected abstract int getLayoutId();
}
