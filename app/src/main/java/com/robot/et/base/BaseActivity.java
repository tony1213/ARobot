package com.robot.et.base;

import android.app.Activity;
import android.os.Bundle;

import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.lib.core.picasso.ImageLoader;
import com.slamtec.slamware.SlamwareCorePlatform;

public class BaseActivity extends Activity {

    public SlamwareCorePlatform slamwareCorePlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPicasso();
        initSlam();
    }
    private void initPicasso(){
        ImageLoader.getInstance().initContext(this);
    }

    private void initSlam(){
        slamwareCorePlatform = SlamtecLoader.getInstance().execConnect();
    }
}
