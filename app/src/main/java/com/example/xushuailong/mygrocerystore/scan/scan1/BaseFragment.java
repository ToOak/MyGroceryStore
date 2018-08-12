package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by guanghui_wan on 2016/11/17.
 */

public abstract class BaseFragment extends Fragment {

    public Activity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setListeners();
        initData();
    }

    public abstract void findViews(View view);

    public abstract void setListeners();

    public abstract void initData();
}
