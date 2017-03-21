package com.cretin.www.caipu.net;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HttpURLConnection网络请求工具类
 */
public class HttpUtil {
    static ExecutorService threadPool = Executors.newCachedThreadPool();
    static Gson gson = new Gson();
//    /**
//     * Get请求
//     */
//    public static void sendGetRequest(
//            final String urlString, final HttpCallbackListener listener) {
//        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                URL url;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    // 根据URL地址创建URL对象
//                    url = new URL(urlString);
//                    // 获取HttpURLConnection对象
//                    httpURLConnection = ( HttpURLConnection ) url.openConnection();
//                    // 设置请求方式，默认为GET
//                    httpURLConnection.setRequestMethod("GET");
//                    // 设置连接超时
//                    httpURLConnection.setConnectTimeout(5000);
//                    // 设置读取超时
//                    httpURLConnection.setReadTimeout(8000);
//                    // 响应码为200表示成功，否则失败。
//                    if ( httpURLConnection.getResponseCode() != 200 ) {
//
//                    }
//                    // 获取网络的输入流
//                    InputStream is = httpURLConnection.getInputStream();
//                    // 读取输入流中的数据
//                    BufferedInputStream bis = new BufferedInputStream(is);
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    byte[] bytes = new byte[1024];
//                    int len = -1;
//                    while ( (len = bis.read(bytes)) != -1 ) {
//                        baos.write(bytes, 0, len);
//                    }
//                    bis.close();
//                    is.close();
//                    // 响应的数据
//                    byte[] response = baos.toByteArray();
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onFinish(response);
//                    }
//                } catch ( MalformedURLException e ) {
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } catch ( IOException e ) {
//
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } finally {
//                    if ( httpURLConnection != null ) {
//                        // 释放资源
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//        });
//    }


//    /**
//     * Get请求
//     */
//    public static void sendGetStringRequest(
//            final String urlString, final HttpCallbackStringListener listener) {
//        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                URL url;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    // 根据URL地址创建URL对象
//                    url = new URL(urlString);
//                    // 获取HttpURLConnection对象
//                    httpURLConnection = ( HttpURLConnection ) url.openConnection();
//                    // 设置请求方式，默认为GET
//                    httpURLConnection.setRequestMethod("GET");
//                    // 设置连接超时
//                    httpURLConnection.setConnectTimeout(5000);
//                    // 设置读取超时
//                    httpURLConnection.setReadTimeout(8000);
//                    // 响应码为200表示成功，否则失败。
//                    if ( httpURLConnection.getResponseCode() != 200 ) {
//
//                    }
//                    // 获取网络的输入流
//                    InputStream is = httpURLConnection.getInputStream();
//                    BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                    //最好在将字节流转换为字符流的时候 进行转码
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while ( (line = bf.readLine()) != null ) {
//                        buffer.append(line);
//                    }
//                    bf.close();
//                    is.close();
//                    if ( listener != null ) {
//                        // 回调onFinish()方法
//                        listener.onFinish(buffer.toString());
//                    }
//                } catch ( MalformedURLException e ) {
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } catch ( IOException e ) {
//
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } finally {
//                    if ( httpURLConnection != null ) {
//                        // 释放资源
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//        });
//    }

    /**
     * Get请求
     */
    public static <T> void sendGetModelRequest(final Context context,
                                               final String urlString, final HttpCallbackModelListener listener, final Class<T> cls) {
        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    // 根据URL地址创建URL对象
                    url = new URL(urlString);
                    // 获取HttpURLConnection对象
                    httpURLConnection = ( HttpURLConnection ) url.openConnection();
                    // 设置请求方式，默认为GET
                    httpURLConnection.setRequestMethod("GET");
                    // 设置连接超时
                    httpURLConnection.setConnectTimeout(5000);
                    // 设置读取超时
                    httpURLConnection.setReadTimeout(8000);
                    // 响应码为200表示成功，否则失败。
                    if ( httpURLConnection.getResponseCode() != 200 ) {

                    }
                    // 获取网络的输入流
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    //最好在将字节流转换为字符流的时候 进行转码
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ( (line = bf.readLine()) != null ) {
                        buffer.append(line);
                    }
                    bf.close();
                    is.close();
                    new ResponseCall(context,listener).doGet(gson.fromJson(buffer.toString(), cls));
                } catch ( MalformedURLException e ) {
                    if ( listener != null ) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } catch ( IOException e ) {

                    if ( listener != null ) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if ( httpURLConnection != null ) {
                        // 释放资源
                        httpURLConnection.disconnect();
                    }
                }
            }
        });
    }

//    /**
//     * Post请求
//     */
//    public static void sendPostRequest(
//            final String urlString, final HttpCallbackListener listener) {
//
//        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                URL url;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    url = new URL(urlString);
//                    httpURLConnection = ( HttpURLConnection ) url.openConnection();
//
//                    httpURLConnection.setRequestMethod("POST");
//
//                    httpURLConnection.setConnectTimeout(5000);
//                    httpURLConnection.setReadTimeout(8000);
//
//                    // 设置运行输入
//                    httpURLConnection.setDoInput(true);
//                    // 设置运行输出
//                    httpURLConnection.setDoOutput(true);
//
//                    // 请求的数据
//                    String data = "num=" + URLEncoder.encode("10", "UTF-8") +
//                            "&page=" + URLEncoder.encode("1", "UTF-8");
//
//                    // 将请求的数据写入输出流中
//                    OutputStream os = httpURLConnection.getOutputStream();
//                    BufferedOutputStream bos = new BufferedOutputStream(os);
//                    bos.write(data.getBytes());
//                    bos.flush();
//                    bos.close();
//                    os.close();
//
//                    if ( httpURLConnection.getResponseCode() == 200 ) {
//
//                        InputStream is = httpURLConnection.getInputStream();
//                        BufferedInputStream bis = new BufferedInputStream(is);
//
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        byte[] bytes = new byte[1024];
//                        int len = -1;
//                        while ( (len = bis.read(bytes)) != -1 ) {
//                            baos.write(bytes, 0, len);
//                        }
//                        is.close();
//                        bis.close();
//                        // 响应的数据
//                        byte[] response = baos.toByteArray();
//
//                        if ( listener != null ) {
//                            // 回调onFinish()方法
//                            listener.onFinish(response);
//                        }
//                    } else {
////                        Logger.e("请求失败");
//                    }
//                } catch ( MalformedURLException e ) {
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } catch ( IOException e ) {
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } finally {
//                    if ( httpURLConnection != null ) {
//                        // 最后记得关闭连接
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * Post请求
//     */
//    public static void sendPostStringRequest(
//            final String urlString, final HttpCallbackStringListener listener) {
//
//        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                URL url;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    url = new URL(urlString);
//                    httpURLConnection = ( HttpURLConnection ) url.openConnection();
//
//                    httpURLConnection.setRequestMethod("POST");
//
//                    httpURLConnection.setConnectTimeout(5000);
//                    httpURLConnection.setReadTimeout(8000);
//
//                    // 设置运行输入
//                    httpURLConnection.setDoInput(true);
//                    // 设置运行输出
//                    httpURLConnection.setDoOutput(true);
//
//                    // 请求的数据
//                    String data = "num=" + URLEncoder.encode("10", "UTF-8") +
//                            "&page=" + URLEncoder.encode("1", "UTF-8");
//
//                    // 将请求的数据写入输出流中
//                    OutputStream os = httpURLConnection.getOutputStream();
//                    BufferedOutputStream bos = new BufferedOutputStream(os);
//                    bos.write(data.getBytes());
//                    bos.flush();
//                    bos.close();
//                    os.close();
//
//                    if ( httpURLConnection.getResponseCode() == 200 ) {
//                        // 获取网络的输入流
//                        InputStream is = httpURLConnection.getInputStream();
//                        BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                        //最好在将字节流转换为字符流的时候 进行转码
//                        StringBuffer buffer = new StringBuffer();
//                        String line = "";
//                        while ( (line = bf.readLine()) != null ) {
//                            buffer.append(line);
//                        }
//                        is.close();
//                        bf.close();
//                        if ( listener != null ) {
//                            // 回调onFinish()方法
//                            listener.onFinish(buffer.toString());
//                        }
//                    } else {
////                        Logger.e("请求失败");
//                    }
//                } catch ( MalformedURLException e ) {
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } catch ( IOException e ) {
//                    if ( listener != null ) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } finally {
//                    if ( httpURLConnection != null ) {
//                        // 最后记得关闭连接
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//        });
//    }
}
