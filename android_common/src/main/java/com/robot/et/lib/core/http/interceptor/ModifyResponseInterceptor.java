package com.robot.et.lib.core.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

//modify the response's header
public final class ModifyResponseInterceptor implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .addHeader("Cache-Control", "max-age=600")
                .build();
    }
}
