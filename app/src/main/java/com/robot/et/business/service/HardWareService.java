package com.robot.et.business.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.robot.et.R;
import com.robot.et.business.control.FollowBody;
import com.robot.et.business.media.Music;
import com.robot.et.core.software.vision.Vision;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.hardware.wakeup.IWakeUp;
import com.robot.et.core.hardware.wakeup.WakeUpHandler;
import com.robot.et.core.software.slam.SlamtecLoader;

import java.util.Random;

/**
 * Created by houdeming on 2016/11/11.
 * 硬件相关
 */

public class HardWareService extends Service implements IWakeUp {

    private static final String TAG = "HardWareService";
    private WakeUpHandler wakeUpHandler;
    private static final int VOICE_AWAKEN = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wakeUpHandler = new WakeUpHandler(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getVoiceWakeUpDegree(int degree) {
        Log.i(TAG, "degree===" + degree);
        Message msg = handler.obtainMessage();
        msg.what = VOICE_AWAKEN;
        msg.arg1 = degree;
        handler.sendMessage(msg);
    }

    @Override
    public void bodyDetection() {

    }

    @Override
    public void bodyTouch(int touchId) {

    }

    @Override
    public void shortPress() {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VOICE_AWAKEN:
                    awakenNeedStop();
                    if (GlobalConfig.isConnectSlam) {
                        SlamtecLoader.getInstance().execBasicRotate(msg.arg1);
                    }
                    Log.i(TAG, "说内容");
                    VoiceHandler.speakEndToListen(getAwakenContent());
                    break;
            }
        }
    };

    /**
     * 唤醒的处理
     */
    private void awakenNeedStop() {
        // 停止说
        VoiceHandler.stopSpeak();
        // 停止听
        VoiceHandler.stopListen();
        FollowBody.getInstance().stopFollow();
        if (GlobalConfig.isVisionLearn) {
            GlobalConfig.isVisionLearn = false;
            Vision.getInstance().closeLearn();
        }
        if (GlobalConfig.isPlayMusic) {
            GlobalConfig.isPlayMusic = false;
            Music.stopMusic();
        }
    }

    /**
     * 获取唤醒时要说的内容
     *
     * @return
     */
    private String getAwakenContent() {
        String content = "";
        String[] wakeUpSpeakContent = getResources().getStringArray(R.array.wake_up_speak_content);
        int size = wakeUpSpeakContent.length;
        if (wakeUpSpeakContent != null && size > 0) {
            int i = new Random().nextInt(size);
            content = wakeUpSpeakContent[i];
        }
        return content;
    }
}
