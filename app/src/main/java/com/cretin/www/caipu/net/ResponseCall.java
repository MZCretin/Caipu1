package com.cretin.www.caipu.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by cretin on 2017/3/20.
 */

public class ResponseCall<T> {
    Handler mHandler;

    public ResponseCall(Context context, final HttpCallbackModelListener listener) {
        Looper looper = context.getMainLooper();
        mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listener.onFinish(msg.obj);
            }
        };
    }

    public void doGet(T response) {
        Message message = Message.obtain();
        message.obj = response;
        mHandler.sendMessage(message);
    }
}
