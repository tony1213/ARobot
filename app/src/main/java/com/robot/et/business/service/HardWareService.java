package com.robot.et.business.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.robot.et.R;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.core.hardware.wakeup.IWakeUp;
import com.robot.et.core.hardware.wakeup.WakeUpHandler;

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
        awakenNeedStop();
        VoiceHandler.speakEndToListen(getAwakenContent());
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

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
    }

    /**
     * 获取唤醒时要说的内容
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
