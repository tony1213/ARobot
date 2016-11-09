package com.robot.et.core.software.voice;

import android.content.Context;

import com.robot.et.core.software.voice.impl.ifly.IflyVoiceImpl;
import com.robot.et.core.software.voice.impl.turing.TuringVoiceImpl;

/**
 * Created by houdeming on 2016/11/2.
 * 语音工厂类
 */
public class VoiceFactory {

    private static IflyVoiceImpl iflyVoice;
    private static TuringVoiceImpl turingVoice;

    /**
     * 创建科大讯飞的语音
     * @param context 上下文
     * @return
     */
    public static IVoice produceIflyVoice(Context context) {
        if (iflyVoice == null) {
            iflyVoice = new IflyVoiceImpl(context);
        }
        return iflyVoice;
    }

    /**
     * 创建图灵的语音
     * @param context 上下文
     * @return
     */
    public static IVoice produceTuringVoice(Context context) {
        if (turingVoice == null) {
            turingVoice = new TuringVoiceImpl(context);
        }
        return turingVoice;
    }
}
