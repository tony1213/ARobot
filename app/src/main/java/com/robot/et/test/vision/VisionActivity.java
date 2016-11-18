package com.robot.et.test.vision;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.robot.et.R;
import com.robot.et.core.software.vision.Vision;
import com.robot.et.core.software.vision.callback.BodyPositionCallBack;
import com.robot.et.core.software.vision.callback.LearnOpenCallBack;
import com.robot.et.core.software.vision.callback.VisionBitmapCallBack;
import com.robot.et.core.software.vision.callback.VisionImgInfoCallBack;
import com.robot.et.core.software.vision.callback.VisionInitCallBack;
import com.robot.et.core.software.vision.callback.VisionLearnCallBack;
import com.robot.et.core.software.vision.callback.VisionRecogniseCallBack;
import com.robot.et.util.TimerManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by houdeming on 2016/11/12.
 */

public class VisionActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "visionHand";
    private ImageView imgVision;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_test);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button init = (Button) findViewById(R.id.btn_init);
        Button uInit = (Button) findViewById(R.id.btn_uinit);
        Button learn = (Button) findViewById(R.id.btn_learn);
        Button call = (Button) findViewById(R.id.btn_callback);
        Button pos = (Button) findViewById(R.id.btn_bodyPos);
        Button recog = (Button) findViewById(R.id.btn_recog);
        Button learnOpen = (Button) findViewById(R.id.btn_learn_open);
        Button learnClose = (Button) findViewById(R.id.btn_learn_close);
        Button posOpen = (Button) findViewById(R.id.btn_bodyPos_open);
        Button posClose = (Button) findViewById(R.id.btn_bodyPos_close);
        Button btnBitmap = (Button) findViewById(R.id.btn_bitmap);
        imgVision = (ImageView) findViewById(R.id.img_vision);
        init.setOnClickListener(this);
        uInit.setOnClickListener(this);
        learn.setOnClickListener(this);
        call.setOnClickListener(this);
        pos.setOnClickListener(this);
        recog.setOnClickListener(this);
        learnOpen.setOnClickListener(this);
        learnClose.setOnClickListener(this);
        posOpen.setOnClickListener(this);
        posClose.setOnClickListener(this);
        btnBitmap.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                Log.i(TAG, "initVision");
                Vision.getInstance().initVision(new VisionInitCallBack() {
                    @Override
                    public void onVisionInitResult(boolean isSuccess) {
                        Log.i(TAG, "isSuccess==" + isSuccess);
                        if (isSuccess) {
                            Vision.getInstance().getImgInfo(new VisionImgInfoCallBack() {
                                @Override
                                public void onVisionImgInfo(int width, int height, int dataType) {
                                    Vision.getInstance().sendBitmapToFramework(width, height);
                                }
                            });

                        }
                    }
                });

                break;
            case R.id.btn_uinit:
                Log.i(TAG, "unInitVision");
                Vision.getInstance().unInitVision();

                break;
            case R.id.btn_learn_open:
                Log.i(TAG, "openLearn");
                Vision.getInstance().openLearn(new LearnOpenCallBack() {
                    @Override
                    public void onLearnOpenEnd() {
                        Log.i(TAG, "onLearnOpenEnd");
                    }
                });

                break;
            case R.id.btn_learn_close:
                Log.i(TAG, "closeLearn");
                Vision.getInstance().closeLearn();

                break;
            case R.id.btn_learn:
                Log.i(TAG, "startLearn");
                Vision.getInstance().startLearn("android", new VisionLearnCallBack() {
                    @Override
                    public void onLearnEnd() {
                        Log.i(TAG, "onLearnEnd");
                    }
                });

                break;
            case R.id.btn_callback:

                break;
            case R.id.btn_bodyPos_open:
                Log.i(TAG, "openBodyDetect");
                Vision.getInstance().openBodyDetect();

                break;
            case R.id.btn_bodyPos_close:
                Log.i(TAG, "closeBodyDetect");
                Vision.getInstance().closeBodyDetect();

                break;
            case R.id.btn_bodyPos:
                Log.i(TAG, "getBodyPosition");
                Vision.getInstance().getBodyPosition(new BodyPositionCallBack() {
                    @Override
                    public void onBodyPosition(float centerX, float centerY, float centerZ) {
                        Log.i(TAG, "centerX=" + centerX + "--centerY==" + centerY + "--centerZ==" + centerZ);
                    }
                });

                break;
            case R.id.btn_recog:
                Log.i(TAG, "startRecognise");
                Vision.getInstance().startRecognise(new VisionRecogniseCallBack() {
                    @Override
                    public void onVisionRecogniseResult(boolean isRecogniseSuccess, String speakContent) {
                        Log.i(TAG, "isRecogniseSuccess==" + isRecogniseSuccess + "--speakContent==" + speakContent);
                    }
                });

                break;
            case R.id.btn_bitmap:
                Log.i(TAG, "getVisionBitmap");

                timer = TimerManager.createTimer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                }, 0, 100);

                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Vision.getInstance().getVisionBitmap(new VisionBitmapCallBack() {
                        @Override
                        public void onVisionBitmap(Bitmap bitmap) {
                            Log.i(TAG, "111");
                            if (bitmap != null) {
                                imgVision.setImageBitmap(bitmap);
                            }
                        }
                    });
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            TimerManager.cancelTimer(timer);
            timer = null;
        }
        Vision.getInstance().unInitVision();
    }
}
