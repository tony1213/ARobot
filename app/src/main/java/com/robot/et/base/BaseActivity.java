package com.robot.et.base;

import android.app.Activity;
import android.os.Bundle;

import com.robot.et.lib.core.picasso.ImageLoader;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPicasso();
    }
    private void initPicasso(){
        ImageLoader.getInstance().initContext(this);
    }
}
