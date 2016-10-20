package com.robot.et.lib.core.http;

import com.robot.et.lib.business.api.GitHubAPI;

/**
 * Created by Tony on 2016/10/17.
 */

public enum  HttpTaskHelper {

    INSTANCE;

    private static GitHubAPI gitHubAPI;

    HttpTaskHelper() {
    }

    public static GitHubAPI gitHubAPI() {
        if (gitHubAPI == null) {
            gitHubAPI = RetrofitClient.INSTANCE.getRetrofit().create(GitHubAPI.class);
        }
        return gitHubAPI;
    }
}
