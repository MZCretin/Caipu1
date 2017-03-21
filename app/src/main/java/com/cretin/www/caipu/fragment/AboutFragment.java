package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {
    public static final String TAG = "AboutFragment";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle("关于我们");
    }

    @Override
    protected void initData() {

    }
}
