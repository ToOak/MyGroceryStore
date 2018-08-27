package com.example.xushuailong.mygrocerystore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.xushuailong.mygrocerystore.fragment.Tab1Fragment;
import com.example.xushuailong.mygrocerystore.utils.ListUtil;

import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {

    private List<String> names;

    public TabAdapter(FragmentManager fm, List<String> names) {
        super(fm);
        this.names = names;
    }

    @Override
    public Fragment getItem(int position) {
        return Tab1Fragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return ListUtil.getSize(names);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }
}
