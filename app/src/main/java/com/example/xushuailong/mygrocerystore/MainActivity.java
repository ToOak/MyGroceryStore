package com.example.xushuailong.mygrocerystore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView tv;

    @Override
    protected int getlayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv = (TextView) findViewById(R.id.tv_main);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwoActivity.class);
                //获取控件在屏幕中的坐标
                int location[] = new int[2];
                tv.getLocationOnScreen(location);
                intent.putExtra("x", location[0]);
                intent.putExtra("y", location[1]);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}
