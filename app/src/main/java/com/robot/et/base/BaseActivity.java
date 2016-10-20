package com.robot.et.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robot.et.lib.core.picasso.ImageLoader;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPicasso();
    }
    private void initPicasso(){
        ImageLoader.getInstance().initContext(this);
    }
}
