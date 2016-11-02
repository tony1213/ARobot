package com.robot.et.core.software.voice;

import android.content.Context;

import com.robot.et.core.software.voice.impl.ifly.IflyVoiceImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class IflyVoiceFactory implements VoiceFactory {
    @Override
    public IVoice createVoice(Context context) {
        return new IflyVoiceImpl(context);
    }
}
