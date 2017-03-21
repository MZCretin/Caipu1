package com.cretin.www.caipu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BaseActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.fragment.ClassifyFragment;
import com.cretin.www.caipu.fragment.HomeFragment;
import com.cretin.www.caipu.fragment.MeFragment;
import com.cretin.www.caipu.fragment.TestFragment;
import com.cretin.www.caipu.utils.UiUtils;
import com.cretin.www.caipu.view.NoScrollViewPager;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Bind( R.id.view_pager )
    NoScrollViewPager viewPager;
    @Bind( R.id.rb_home )
    RadioButton rbHome;
    @Bind( R.id.rb_product )
    RadioButton rbProduct;
    @Bind( R.id.rb_message )
    RadioButton rbMessage;
    @Bind( R.id.rg_group )
    RadioGroup rgGroup;
    private Map<Integer, BaseFragment> mFragments = new HashMap();
    private int currentPage = 0;
    private int lastPage;

    @Override
    protected void initView(View view) {
        if ( view != null ) {
            hidTitleView();
        }
    }

    @Override
    protected void initData() {
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        //ViewPager缓存4个界面
        viewPager.setOffscreenPageLimit(3);
        rgGroup.check(R.id.rb_home);

        // 监听RadioGroup的选择事件
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                lastPage = currentPage;
                switch ( checkedId ) {
                    case R.id.rb_home:
                        currentPage = 0;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    case R.id.rb_product:
                        currentPage = 1;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    case R.id.rb_message:
                        currentPage = 2;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    private long lastBackTime;

    //在需要监听的activity中重写onKeyDown()。
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0 ) {
            long currentTime = System.currentTimeMillis();
            if ( currentTime - lastBackTime > 1 * 1000 ) {
                lastBackTime = currentTime;
                UiUtils.showToastInAnyThread("再按一次退出程序");
            } else {
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private class MainAdapter extends FragmentStatePagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        // 每个条目返回的fragment
        //  0
        @Override
        public Fragment getItem(int position) {
            return createFragment(position);
        }

        // 一共有几个条目
        @Override
        public int getCount() {
            return 3;
        }

        // 返回每个条目的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    public BaseFragment createFragment(int position) {
        BaseFragment fragment;
        fragment = mFragments.get(position);
        //在集合中取出来Fragment
        if ( fragment == null ) {  //如果再集合中没有取出来 需要重新创建
            if ( position == 0 ) {
                fragment = new HomeFragment();
            } else if ( position == 1 ) {
                fragment = new ClassifyFragment();
            } else if ( position == 2 ) {
                fragment = new MeFragment();
            }
            if ( fragment != null ) {
                mFragments.put(position, fragment);// 把创建好的Fragment存放到集合中缓存起来
            }
            return fragment;
        } else {
            return fragment;
        }
    }
}
