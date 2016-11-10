package com.robot.et.lib.core.http;

import com.robot.et.lib.core.rxjava.RxJavaCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum RetrofitClient {

    INSTANCE;

    private final Retrofit retrofit;

    RetrofitClient() {
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(OKHttpFactory.INSTANCE.getOkHttpClient())

                //baseUrl:https://api.github.com/
                //test:http://192.168.1.28:8080/robot-app/
                //cloud:http://139.196.171.217:8080/robot-app/
                .baseUrl("http://139.196.171.217:8080/robot-app/")

                //string转化器
//                .addConverterFactory(StringConverter.create())

                //gson转化器
                .addConverterFactory(GsonConverterFactory.create())

                //Rx
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                //创建
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}