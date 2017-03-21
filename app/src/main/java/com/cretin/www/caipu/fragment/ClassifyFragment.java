package com.cretin.www.caipu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.base.BaseFragmentActivity;
import com.cretin.www.caipu.ui.MeManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyFragment extends BaseFragment {


    @Bind( R.id.ll_jcc )
    LinearLayout llJcc;
    @Bind( R.id.ll_cyc )
    LinearLayout llCyc;
    @Bind( R.id.ll_ksc )
    LinearLayout llKsc;
    @Bind( R.id.ll_sc )
    LinearLayout llSc;
    @Bind( R.id.ll_lc )
    LinearLayout llLc;
    @Bind( R.id.ll_hp )
    LinearLayout llHp;
    @Bind( R.id.ll_ms )
    LinearLayout llMs;

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

    @OnClick( {R.id.ll_jcc, R.id.ll_cyc, R.id.ll_ksc, R.id.ll_sc, R.id.ll_lc, R.id.ll_hp, R.id.ll_ms} )
    public void onClick(View view) {
        Intent intent = new Intent(mActivity, MeManager.class);
        intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, ClassifyDetailsFragment.TAG);
        Bundle bundle = new Bundle();
        switch ( view.getId() ) {
            case R.id.ll_jcc:
                bundle.putString("title", "家常菜");
                break;
            case R.id.ll_cyc:
                bundle.putString("title", "创意菜");
                break;
            case R.id.ll_ksc:
                bundle.putString("title", "快手菜");
                break;
            case R.id.ll_sc:
                bundle.putString("title", "素食");
                break;
            case R.id.ll_lc:
                bundle.putString("title", "凉菜");
                break;
            case R.id.ll_hp:
                bundle.putString("title", "烘焙");
                break;
            case R.id.ll_ms:
                bundle.putString("title", "面食");
                break;
        }
        intent.putExtra(BaseFragmentActivity.ARGS, bundle);
        mActivity.startActivity(intent);
    }
}
