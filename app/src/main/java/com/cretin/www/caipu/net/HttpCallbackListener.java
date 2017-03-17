package com.cretin.www.caipu.net;

/**
 * HttpURLConnection网络请求返回监听器
 */
public interface HttpCallbackListener {
    // 网络请求成功
    void onFinish(byte[] response);

    // 网络请求失败
    void onError(Exception e);
}
