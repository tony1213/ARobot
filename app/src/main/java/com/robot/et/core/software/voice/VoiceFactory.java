package com.robot.et.core.software.voice;

import android.content.Context;

/**
 * Created by houdeming on 2016/11/2.
 * 语音工厂接口
 */
public interface VoiceFactory {
    /**
     * 创建语音
     *
     * @param context 上下文
     * @return
     */
    IVoice createVoice(Context context);
}
