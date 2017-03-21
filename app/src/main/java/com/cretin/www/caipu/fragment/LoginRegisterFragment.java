package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.adapter.TabAdapter;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.eventbus.NotifyLoginRegisterChange;
import com.cretin.www.caipu.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginRegisterFragment extends BaseFragment {
    public static final String TAG = "LoginRegisterFragment";
    @Bind( R.id.tab_login_register )
    TabLayout tabLoginRegister;
    @Bind( R.id.viewpager_login_register )
    ViewPager viewpagerLoginRegister;
    @Bind( R.id.ll_head )
    LinearLayout llHead;
    private TabAdapter adapter;
    private String[] titles = {"登录", "注册"};
    private List<Fragment> list;

    @Override
    protected void initData() {
        list = new ArrayList<>();
        list.add(new LoginFragment());
        list.add(new RegisterFragment());

        adapter = new TabAdapter(mActivity.getSupportFragmentManager(), list);
        adapter.setTabTitle(titles);
        //给ViewPager设置适配器
        viewpagerLoginRegister.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来。
        tabLoginRegister.setupWithViewPager(viewpagerLoginRegister);
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        hidProgressView();
        setMainTitle("登录/注册");
        if ( mActivity.isKitkat ) {
            llHead.setPadding(0, ViewUtils.getStatusBarHeights() - 20, 0, 0);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_register;
    }

    @Subscribe
    public void notifyLoginRegisterChange(NotifyLoginRegisterChange event) {
        if ( event.getCurrStation() == NotifyLoginRegisterChange.STATE_LOGIN ) {
            viewpagerLoginRegister.setCurrentItem(1);
        } else {
            viewpagerLoginRegister.setCurrentItem(0);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
