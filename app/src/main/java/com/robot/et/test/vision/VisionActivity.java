package com.robot.et.test.vision;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.robot.et.R;
import com.robot.et.VisionManager;
import com.robot.et.callback.VisionCallBack;

/**
 * Created by houdeming on 2016/11/12.
 */

public class VisionActivity extends Activity implements View.OnClickListener,VisionCallBack {

    private static final String TAG = "visionTest";
    private VisionManager visionManager;

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

        visionManager = new VisionManager(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                try {
                    int visionId = visionManager.visionInit();
                    Log.i(TAG,"visionId==" + visionId);
                } catch (RemoteException e) {
                    Log.i(TAG,"RemoteException");
                }
                break;
            case R.id.btn_uinit:
                try {
                    Log.i(TAG,"visionUninit");
                    visionManager.visionUninit();
                } catch (RemoteException e) {
                    Log.i(TAG,"visionUninit RemoteException");
                }

                break;
            case R.id.btn_learn_open:
                try {
                    Log.i(TAG,"visionLearnOpen");
                    visionManager.visionLearnOpen();
                } catch (RemoteException e) {
                    Log.i(TAG,"Learn RemoteException");
                }
                break;
            case R.id.btn_learn_close:
                try {
                    Log.i(TAG,"visionLearnClose");
                    visionManager.visionLearnClose();
                } catch (RemoteException e) {
                    Log.i(TAG,"Learn RemoteException");
                }
                break;
            case R.id.btn_learn:
                try {
                    Log.i(TAG,"Learn");
                    String str = "android";
                    visionManager.objLearnStartLearn(str);
                } catch (RemoteException e) {
                    Log.i(TAG,"Learn RemoteException");
                }
                break;
            case R.id.btn_callback:
                try {
                    Log.i(TAG,"Callback");
                    visionManager.testCallback();
                } catch (RemoteException e) {
                    Log.i(TAG,"Callback RemoteException");
                }
                break;
            case R.id.btn_bodyPos_open:
                try {
                    Log.i(TAG,"bodyDetectOpen");
                    visionManager.bodyDetectOpen();
                } catch (RemoteException e) {
                    Log.i(TAG,"bodyDetectGetPos RemoteException");
                }
                break;
            case R.id.btn_bodyPos_close:
                try {
                    Log.i(TAG,"bodyDetectClose");
                    visionManager.bodyDetectClose();
                } catch (RemoteException e) {
                    Log.i(TAG,"bodyDetectGetPos RemoteException");
                }
                break;
            case R.id.btn_bodyPos:
                try {
                    Log.i(TAG,"bodyDetectGetPos");
                    visionManager.bodyDetectGetPos();
                } catch (RemoteException e) {
                    Log.i(TAG,"bodyDetectGetPos RemoteException");
                }
                break;
            case R.id.btn_recog:
                try {
                    Log.i(TAG,"Recog");
                    visionManager.objLearnStartRecog();
                } catch (RemoteException e) {
                    Log.i(TAG,"Recog RemoteException");
                }
                break;
        }
    }

    @Override
    public void learnOpenEnd() {
        Log.i(TAG,"learnOpenEnd");
    }

    @Override
    public void learnWaring(int id) {
        Log.i(TAG,"learnWaring id==" + id);
    }

    @Override
    public void learnEnd() {
        Log.i(TAG,"learnEnd");
    }

    @Override
    public void learnRecogniseEnd(String name, int conf) {
        Log.i(TAG,"learnRecogniseEnd name==" + name + "---conf===" + conf);
    }

    @Override
    public void bodyPosition(float centerX, float centerY, float centerZ) {
        Log.i(TAG,"X==" + centerX + "--Y==" + centerY + "--Z==" + centerZ);
    }
}
