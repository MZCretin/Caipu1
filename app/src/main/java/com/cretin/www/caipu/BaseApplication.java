package com.cretin.www.caipu;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by wanglei on 2016/12/9.
 */

public class BaseApplication extends Application {
    private static Context context;
    private static int mainTid;
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        mainTid = android.os.Process.myTid();
        handler = new Handler();
        context = getApplicationContext();

        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("3e2ee805a390e7500aecd71475537043")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        //初始化Hawk
        initHawk();
    }

    //手动配置Hawk
    private void initHawk() {
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
    }

    public static Context getContext() {
        return context;
    }

    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return handler;
    }
}
