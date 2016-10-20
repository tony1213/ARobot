package com.robot.et.lib;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.analytics.MobclickAgent;

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext= this;
        Fresco.initialize(this);
        MobclickAgent. startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,"58059153e0f55a317b002131","Wandoujia", MobclickAgent.EScenarioType.E_UM_NORMAL,true));
    }
}
