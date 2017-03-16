package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BaseFragment;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {
    @Bind( R.id.textview )
    TextView textview;
    private int code = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {

    }

}
