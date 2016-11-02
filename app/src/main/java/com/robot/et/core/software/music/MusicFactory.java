package com.robot.et.core.software.music;

import android.content.Context;

/**
 * Created by houdeming on 2016/11/2.
 * 音乐播放的工厂接口
 */
public interface MusicFactory {
    /**
     * 创建音乐
     * @param context 上下文
     * @return
     */
    IMusic createMusic(Context context);
}
