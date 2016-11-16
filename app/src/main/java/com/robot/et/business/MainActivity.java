package com.robot.et.business;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.robot.et.R;
import com.robot.et.business.service.HardWareService;
import com.robot.et.business.view.ViewManager;
import com.robot.et.business.view.callback.ViewCallBack;
import com.robot.et.business.vision.Vision;
import com.robot.et.business.vision.callback.VisionInitCallBack;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.push.ali.ALiPush;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.core.software.widget.CustomTextView;
import com.slamtec.slamware.SlamwareCorePlatform;

public class MainActivity extends Activity implements ViewCallBack {

    private static final String TAG = "mainInit";
    private LinearLayout showTextL, showEmotionL, showImgL;
    private ImageView imgEmotion, imageBitmap, imagePhoto;
    private CustomTextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 初始化view
        initView();
        // 设置view的接口回调
        ViewManager.setViewCallBack(this);
        // 初始化service
        initService();
        initSlam();
        initVision();

    }

    /**
     * 初始化view
     */
    private void initView() {
        showTextL = (LinearLayout) findViewById(R.id.ll_show_text);
        showEmotionL = (LinearLayout) findViewById(R.id.ll_show_emotion);
        showImgL = (LinearLayout) findViewById(R.id.ll_show_img);
        tvText = (CustomTextView) findViewById(R.id.tv_text);
        imgEmotion = (ImageView) findViewById(R.id.img_emotion);
        imageBitmap = (ImageView) findViewById(R.id.img_bitmap);
        imagePhoto = (ImageView) findViewById(R.id.img_photo);

        showEmotionL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                VoiceHandler.listen();
                // 初始化阿里推送
                new ALiPush(MainActivity.this, "80000057");
            }
        });
    }

    /**
     * 初始化slam
     */
    private void initSlam() {
        GlobalConfig.isConnectSlam = false;
        SlamwareCorePlatform slamwareCorePlatform = SlamtecLoader.getInstance().execConnect();
        if (slamwareCorePlatform != null) {
            GlobalConfig.isConnectSlam = true;
            Log.i(TAG, "slam初始化成功");
        }
    }

    /**
     * 初始化视觉
     */
    private void initVision() {
        GlobalConfig.isConnectVision = false;
        Vision.getInstance().initVision(new VisionInitCallBack() {
            @Override
            public void onVisionInitResult(boolean isSuccess) {
                if (isSuccess) {
                    GlobalConfig.isConnectVision = true;
                    Log.i(TAG, "视觉初始化成功");
                }
            }
        });
    }

    private void initService() {
        startService(new Intent(this, HardWareService.class));
    }

    private void stopService() {
        stopService(new Intent(this, HardWareService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceHandler.destroyVoice();
        stopService();
    }

    private void initLinearLayout() {
        showTextL.setVisibility(View.GONE);
        showEmotionL.setVisibility(View.GONE);
        showImgL.setVisibility(View.GONE);
    }

    private void playAnimEmotion() {
        AnimationDrawable animationDrawable = (AnimationDrawable) imgEmotion.getBackground();
        animationDrawable.stop();
        animationDrawable.start();
    }

    @Override
    public void onShowText(String content) {
        initLinearLayout();
        showTextL.setVisibility(View.VISIBLE);
        tvText.setText(content);
    }

    @Override
    public void onShowEmotion(boolean isEmotionAnim, int resId) {
        initLinearLayout();
        showEmotionL.setVisibility(View.VISIBLE);
        imgEmotion.setImageResource(resId);
        if (isEmotionAnim) {
            playAnimEmotion();
        }
    }

    @Override
    public void onShowImg(boolean isNeedAnim, Bitmap bitmap) {
        initLinearLayout();
        showImgL.setVisibility(View.VISIBLE);
        if (isNeedAnim) {
            imageBitmap.setVisibility(View.GONE);
            imagePhoto.setVisibility(View.VISIBLE);
            imagePhoto.setImageBitmap(bitmap);
        } else {
            imagePhoto.setVisibility(View.GONE);
            imageBitmap.setVisibility(View.VISIBLE);
            imageBitmap.setImageBitmap(bitmap);
        }
    }
}
