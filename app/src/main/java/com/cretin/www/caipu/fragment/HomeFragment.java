package com.cretin.www.caipu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.ui.MainSearchActivity;
import com.cretin.www.caipu.webview.TbWebViewActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment{

    @Bind( R.id.tv_search )
    TextView tvSearch;
    @Bind( R.id.rela_ad )
    RelativeLayout relaAd;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        hidTitleView();
    }

    @Override
    protected void initData() {

    }

    @OnClick( {R.id.tv_search, R.id.rela_ad} )
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.tv_search:
                Intent intent = new Intent(mActivity, MainSearchActivity.class);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_bottom_out);
                break;
            case R.id.rela_ad:
                TbWebViewActivity.startActivity(mActivity,
                        "养生之道", "https://m.xiangha.com/zhishi/239182.html");
                break;
        }
    }
}
