package com.robot.et.core.software.slam.base;

import android.app.Activity;
import android.os.Bundle;

import com.robot.et.app.CustomApplication;
import com.slamtec.slamware.SlamwareCorePlatform;

public class BaseActivity extends Activity {

    private CustomApplication application;
    public SlamwareCorePlatform slamwareCorePlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (CustomApplication)getApplication();
        slamwareCorePlatform = application.getSlamwareCorePlatform();
    }
}
