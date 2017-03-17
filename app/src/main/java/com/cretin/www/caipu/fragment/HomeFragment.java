package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.ResponseModel;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.net.HttpCallbackModelListener;
import com.cretin.www.caipu.net.HttpUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

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
        final long t = System.currentTimeMillis();
        Log.e("HHHHH", t + "");
        HttpUtil.sendGetModelRequest("http://caipu.yjghost.com/index.php/query/read?menu=%E5%88%9B%E6%84%8F%E8%8F%9C&rn=15&start=0", new HttpCallbackModelListener<ResponseModel>() {
            @Override
            public void onFinish(ResponseModel response) {
                Log.e("", "");
                tvSearch.setText(response.getResult().getTotalNum()+"");
            }

            @Override
            public void onError(Exception e) {

            }
        }, ResponseModel.class);
    }

    @OnClick( {R.id.tv_search, R.id.rela_ad} )
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.tv_search:
                break;
            case R.id.rela_ad:
                break;
        }
    }
}
