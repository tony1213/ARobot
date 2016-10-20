package com.robot.et.picasso;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.robot.et.R;
import com.robot.et.base.BaseActivity;
import com.robot.et.lib.core.picasso.ImageLoader;

public class PicassoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
        ImageLoader.getInstance().displayImage("http://i.imgur.com/DvpvklR.png",(ImageView) findViewById(R.id.imageView));
    }
}
