package com.fangrongjie.www.srjie.interfaces;


import com.fangrongjie.www.srjie.model.WeatherRequest;
import com.fangrongjie.www.srjie.model.WeatherResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @Author: cretin
 * @Data: 16/4/12 下午2:57
 * @Description:  网络请求接口
 */
public interface INetInterface {
    /**
     * get 请求
     * @param city
     * @return
     */
    @GET("data/cityinfo/{city_id}")
    Observable<WeatherResponse> getWeather(@Path( "city_id" ) String city);

    /**
     * post 请求
     * @param body javabean请求体
     * @return
     */
    @POST("")
    Observable<WeatherResponse> postWeather(@Body WeatherRequest body);

    /**
     * post请求
     * @param params 表单
     * @return
     */
    @POST("")
    Observable<WeatherResponse> postWeather2(@FieldMap Map<String, String> params);
}
