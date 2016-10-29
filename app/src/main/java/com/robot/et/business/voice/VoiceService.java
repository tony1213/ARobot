package com.robot.et.business.voice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.robot.et.core.software.voice.callback.ListenCallBack;
import com.robot.et.core.software.voice.callback.SceneServiceEnum;
import com.robot.et.core.software.voice.callback.UnderstandCallBack;
import com.robot.et.core.software.voice.impl.ifly.XFVoice;
import com.robot.et.core.software.voice.impl.ifly.util.SpeakConfig;
import com.robot.et.core.software.voice.impl.turing.TuringVoice;

/**
 * Created by houdeming on 2016/10/26.
 * 语音业务
 */
public class VoiceService extends Service {
    private XFVoice xfVoice;
    private TuringVoice turingVoice;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        xfVoice = new XFVoice(this);
        turingVoice = new TuringVoice(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        xfVoice.startListen(SpeakConfig.SPEAK_MEN_CLOUD, new ListenCallBack() {
            @Override
            public void onListenBegin() {

            }

            @Override
            public void onListenEnd() {

            }

            @Override
            public void onListenResult(final String result) {
                xfVoice.understanderText(result, new UnderstandCallBack() {
                    @Override
                    public void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult) {
                        if (TextUtils.isEmpty(understandResult)) {
                            turingVoice.understanderText(result, new UnderstandCallBack() {
                                @Override
                                public void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult) {
                                    xfVoice.startSpeak(understandResult, SpeakConfig.SPEAK_MEN_CLOUD, null );
                                }
                            });
                        } else {
                            xfVoice.startSpeak(understandResult, SpeakConfig.SPEAK_MEN_CLOUD, null);
                        }
                    }
                });
            }
        });
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xfVoice.destroyVoice();
    }
}
