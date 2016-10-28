package com.robot.et.lib.core.http;

import android.content.Context;

import com.robot.et.lib.business.api.GitHubAPI;

/**
 * Created by Tony on 2016/10/17.
 */

public enum  HttpTaskHelper {

    INSTANCE;

    private static GitHubAPI gitHubAPI;
    private static Context mContext;

    HttpTaskHelper() {
    }

    public static GitHubAPI gitHubAPI(Context context) {
        mContext = context;
        if (gitHubAPI == null) {
            gitHubAPI = RetrofitClient.INSTANCE.getRetrofit().create(GitHubAPI.class);
        }
        return gitHubAPI;
    }

    public static Context getContext() {
        return mContext;
    }
}
