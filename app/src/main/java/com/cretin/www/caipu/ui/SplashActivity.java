package com.cretin.www.caipu.ui;

import android.content.Intent;
import android.view.View;

import com.cretin.www.caipu.BaseApplication;
import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.ParentActivity;


/**
 * Created by sks on 2016/4/7.
 */
public class SplashActivity extends ParentActivity {
    @Override
    protected void initView(View view) {
        setContentView(R.layout.activity_splash);
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
