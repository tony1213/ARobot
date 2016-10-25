package com.robot.et.lib.core.http;

import com.robot.et.lib.MyApplication;
import com.robot.et.lib.core.http.cookie.CookieManger;
import com.robot.et.lib.core.http.interceptor.GzipRequestInterceptor;
import com.robot.et.lib.core.http.interceptor.ModifyResponseInterceptor;

import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

/**
 * author: baiiu
 * date: on 16/5/16 16:43
 * description:
 */
enum OKHttpFactory {

    INSTANCE;

    private final OkHttpClient okHttpClient;

    private static final int TIMEOUT_READ = 25;
    private static final int TIMEOUT_CONNECTION = 25;

    private static final long CACHE_MAX_SIZE = 10 * 1024 * 1024;

    OKHttpFactory() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Cache cache = new Cache(MyApplication.mContext.getCacheDir(),CACHE_MAX_SIZE);

        CookieJar cookie = new CookieManger(MyApplication.mContext);


        okHttpClient = new OkHttpClient.Builder()
                //打印请求log
                .addInterceptor(httpLoggingInterceptor)
                //修改Request请求信息
                .addInterceptor(new GzipRequestInterceptor())
                //修改Response请求头Cache-Control
                .addInterceptor(new ModifyResponseInterceptor())
                //cookie
                .cookieJar(cookie)
                //cache
                .cache(cache)
                //失败重连
                .retryOnConnectionFailure(true)
                //time out
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)

                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
