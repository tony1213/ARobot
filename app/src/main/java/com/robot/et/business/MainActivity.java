package com.robot.et.business;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.robot.et.R;
import com.robot.et.base.BaseActivity;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.business.voice.callback.ListenResultCallBack;
import com.robot.et.core.software.widget.CustomTextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        LinearLayout showText = (LinearLayout) findViewById(R.id.ll_show_text);
        LinearLayout showEmotion = (LinearLayout) findViewById(R.id.ll_show_emotion);
        LinearLayout showImg = (LinearLayout) findViewById(R.id.ll_show_img);
        CustomTextView tvText = (CustomTextView) findViewById(R.id.tv_text);
        ImageView imgEmotion = (ImageView) findViewById(R.id.img_emotion);
        ImageView imageView = (ImageView) findViewById(R.id.img_bitmap);
        ImageView imagePhoto = (ImageView) findViewById(R.id.img_photo);

        showEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceHandler.listen(new ListenResultCallBack() {
                    @Override
                    public void onListenResult(String result) {
                        VoiceHandler.handleResult(result);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceHandler.destroyVoice();
    }
}
