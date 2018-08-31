package com.example.xushuailong.mygrocerystore.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.base.BaseFragment;

public class Tab1Fragment extends BaseFragment {

    private View tab0;
    private View tab1;
    private View tab2;
    private int position;
    private EditText pwd_et;
    private TextInputLayout pwd_til;
    private Spinner spinner;

    public static final String CURRENT_POSITION = "current_position";

    public static Tab1Fragment newInstance(int position) {
        Tab1Fragment fragment = new Tab1Fragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {
        position = getArguments().getInt(CURRENT_POSITION);
        showCurrentPostion();
    }

    private void showCurrentPostion() {
        tab0.setVisibility(View.GONE);
        tab1.setVisibility(View.GONE);
        tab2.setVisibility(View.GONE);
        switch (position) {
            case 0:
                tab0.setVisibility(View.VISIBLE);
                break;
            case 1:
                tab1.setVisibility(View.VISIBLE);
                break;
            case 2:
                tab2.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initView(View rootView) {
        tab1 = rootView.findViewById(R.id.tab_1);
        tab2 = rootView.findViewById(R.id.tab_2);
        tab0 = rootView.findViewById(R.id.tab_0);

        pwd_til = rootView.findViewById(R.id.pwd_til);
        pwd_et = pwd_til.getEditText();
        pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() > 4) {
                    pwd_til.setError("Password error");
                    pwd_til.setErrorEnabled(true);
                } else {
                    pwd_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner = rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1,
                new String[]{"金星", "木星", "水星", "火星", "土星", "地球"}
        );
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(spinner, position + " " + id + " " + parent.getSelectedItem(), Snackbar.LENGTH_SHORT).show();
            }

            /**
             * 当我们的adapter为空的时候就会调用到这个方法
             * @param parent parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(spinner, " " + parent.getSelectedItem(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_1;
    }
}
