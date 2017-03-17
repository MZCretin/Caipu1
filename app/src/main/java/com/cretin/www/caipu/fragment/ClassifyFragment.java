package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle("菜谱分类");
        hidBackBtn();
    }

    @Override
    protected void initData() {

    }
}
