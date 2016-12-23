package com.fangrongjie.www.srjie.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fangrongjie.www.srjie.R;
import com.fangrongjie.www.srjie.base.BaseActivity;
import com.fangrongjie.www.srjie.fragment.TestFragment;
import com.fangrongjie.www.srjie.utils.UiUtils;
import com.fangrongjie.www.srjie.view.NoScrollViewPager;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

import static com.fangrongjie.www.srjie.R.id.rb_home;

public class MainActivity extends BaseActivity {
    @Bind( R.id.view_pager )
    NoScrollViewPager viewPager;
    @Bind( rb_home )
    RadioButton rbHome;
    @Bind( R.id.rb_data )
    RadioButton rbData;
    @Bind( R.id.rb_message )
    RadioButton rbMessage;
    @Bind( R.id.rb_me )
    RadioButton rbMe;
    @Bind( R.id.rg_group )
    RadioGroup rgGroup;
    private int currentPage = 0;
    private boolean isLogin;
    private int lastPage;
    private Map<Integer, Fragment> mFragments = new HashMap();

    @Override
    protected void initView(View view) {
        if ( view != null ) {
            setListener();
            hidTitleView();
        }
    }

    @Override
    protected void initData() {
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        //ViewPager缓存4个界面
        viewPager.setOffscreenPageLimit(4);
        rgGroup.check(rb_home);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    public void setListener() {
        // 监听RadioGroup的选择事件
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                lastPage = currentPage;
                switch ( checkedId ) {
                    case rb_home:
                        currentPage = 0;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    case R.id.rb_data:
                        currentPage = 1;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    case R.id.rb_message:
                        currentPage = 2;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    case R.id.rb_me:
                        currentPage = 3;
                        viewPager.setCurrentItem(currentPage, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private class MainAdapter extends FragmentStatePagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return createFragment(position);
        }

        @Override
        public int getCount() {
            return 4;
        }


    }

    public Fragment createFragment(int position) {
        Fragment fragment;
        fragment = mFragments.get(position);
        //在集合中取出来Fragment
        if ( fragment == null ) {  //如果再集合中没有取出来 需要重新创建
            if ( position == 0 ) {
                fragment = new TestFragment();
            } else if ( position == 1 ) {
                fragment = new TestFragment();
            } else if ( position == 2 ) {
                fragment = new TestFragment();
            } else if ( position == 3 ) {
                fragment = new TestFragment();
            }
            if ( fragment != null ) {
                mFragments.put(position, fragment);// 把创建好的Fragment存放到集合中缓存起来
            }
            return fragment;
        } else {
            return fragment;
        }
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
}
