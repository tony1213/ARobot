package com.robot.et.fresco;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.robot.et.R;

public class FrescoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);
        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.image_view);
        draweeView.setImageURI(uri);
    }
}
