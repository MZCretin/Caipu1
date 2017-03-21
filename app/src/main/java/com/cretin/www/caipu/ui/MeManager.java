package com.cretin.www.caipu.ui;

import android.os.Bundle;
import android.view.View;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.fragment.AboutFragment;
import com.cretin.www.caipu.fragment.CaipuDetailsFragment;
import com.cretin.www.caipu.fragment.ChangeLoginPswFragment;
import com.cretin.www.caipu.fragment.ClassifyDetailsFragment;
import com.cretin.www.caipu.fragment.LoginRegisterFragment;
import com.cretin.www.caipu.fragment.MyLoveFragment;
import com.cretin.www.caipu.fragment.ResetPswFragment;
import com.cretin.www.caipu.fragment.UserInfoFragment;
import com.cretin.www.caipu.model.ResponseModel;


public class MeManager extends BackFragmentActivity<Bundle> {

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        BaseFragment fragment = null;
        if ( ClassifyDetailsFragment.TAG.equals(tag_fragment) ) {
            fragment = ClassifyDetailsFragment.newInstance(args.getString("title"));
        } else if ( CaipuDetailsFragment.TAG.equals(tag_fragment) ) {
            fragment = CaipuDetailsFragment.newInstance(( ResponseModel.ResultBean.DataBean ) args.getSerializable("model"));
        } else if ( LoginRegisterFragment.TAG.equals(tag_fragment) ) {
            fragment = new LoginRegisterFragment();
        } else if ( ResetPswFragment.TAG.equals(tag_fragment) ) {
            fragment = new ResetPswFragment();
        } else if ( ChangeLoginPswFragment.TAG.equals(tag_fragment) ) {
            fragment = new ChangeLoginPswFragment();
        } else if ( AboutFragment.TAG.equals(tag_fragment) ) {
            fragment = new AboutFragment();
        } else if ( MyLoveFragment.TAG.equals(tag_fragment) ) {
            fragment = new MyLoveFragment();
        } else if ( UserInfoFragment.TAG.equals(tag_fragment) ) {
            fragment = new UserInfoFragment();
        }
        return fragment;
    }

    @Override
    protected void initView(View view) {

    }

//    //在需要监听的activity中重写onKeyDown()。
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ( keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0 ) {
//            List<Fragment> fragments = getSupportFragmentManager().getFragments();
//            if ( fragments.size() > 0 && fragments.get(fragments.size() - 1) != null
//                    && !TextUtils.isEmpty(fragments.get(fragments.size() - 1).getTag()) )
//                if ( fragments.get(fragments.size() - 1).getTag().equals("SendPhoneCodeFragment") ) {
//                    SendPhoneCodeFragment fragment = ( SendPhoneCodeFragment )
//                            fragments.get(fragments.size() - 1);
//                    fragment.removeCallBacks();
//                }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
