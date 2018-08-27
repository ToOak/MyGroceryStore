package com.example.xushuailong.mygrocerystore;

import android.content.Intent;
import android.view.View;

import com.example.xushuailong.mygrocerystore.base.BaseActivity;
import com.example.xushuailong.mygrocerystore.bean.User;
import com.example.xushuailong.mygrocerystore.databinding.ActivityMainBinding;
import com.example.xushuailong.mygrocerystore.utils.LogUtil;

import cn.finalteam.sample.MainGActivity;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {


//        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User user = new User("张君宝", "张三丰", true, 30);
//                mainBinding.setUser(user);
//            }
//        });

        dataBinding.oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("张君宝", "张三丰", true, 16);
                dataBinding.setUser(user);
            }
        });
        dataBinding.twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("郭襄", "襄儿", false, 12);
                dataBinding.setUser(user);
            }
        });
        dataBinding.threeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.setUser(null);
//                Intent intent = new Intent(MainActivity.this, BarcodeScanActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("ScanType", Constant.ScanType.ALL);
//                intent.putExtra(BarcodeScanActivity.KEY_FOCUS_TYPE,Constant.FocusType.AutoFocus);
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, MainGActivity.class);
                startActivity(intent);
            }
        });

        dataBinding.mainTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwoActivity.class);
                //获取控件在屏幕中的坐标
                int location[] = new int[2];
                dataBinding.mainTv.getLocationOnScreen(location);
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
    }
}
