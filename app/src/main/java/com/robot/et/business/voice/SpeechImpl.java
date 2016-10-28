package com.robot.et.business.voice;

/**
 * Created by houdeming on 2016/8/8.
 * 对外提供调用语音
 */
public class SpeechImpl implements ISpeech {
    private static SpeechImpl instance = null;
    private VoiceService mService;

    private SpeechImpl() {
    }

    public static SpeechImpl getInstance() {
        if (instance == null) {
            synchronized (SpeechImpl.class) {
                if (instance == null) {
                    instance = new SpeechImpl();
                }
            }
        }
        return instance;
    }

    public void setService(VoiceService service) {
        this.mService = service;
    }

    @Override
    public void startSpeak(String speakContent, SpeakCallBack callBack) {
        if (mService != null) {
            mService.startSpeak(speakContent, callBack);
        }
    }

    @Override
    public void cancelSpeak() {
        if (mService != null) {
            mService.cancelSpeak();
        }
    }

    @Override
    public void startListen(ListenCallBack callBack) {
        if (mService != null) {
            mService.startListen(callBack);
        }
    }

    @Override
    public void cancelListen() {
        if (mService != null) {
            mService.cancelListen();
        }
    }

    @Override
    public void understanderText(String content, UnderstandCallBack callBack) {
        if (mService != null) {
            mService.understanderText(content, callBack);
        }
    }
}
