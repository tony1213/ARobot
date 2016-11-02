package com.robot.et.core.software.voice;

import android.content.Context;

import com.robot.et.core.software.voice.impl.turing.TuringVoiceImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class TuringVoiceFactory implements VoiceFactory {
    @Override
    public IVoice createVoice(Context context) {
        return new TuringVoiceImpl(context);
    }
}
