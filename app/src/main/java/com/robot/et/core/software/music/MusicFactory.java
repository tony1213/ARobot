package com.robot.et.core.software.music;

import android.content.Context;

import com.robot.et.core.software.music.impl.local.LocalMusicPlayImpl;
import com.robot.et.core.software.music.impl.ximalaya.XiMaLaYaImpl;

/**
 * Created by houdeming on 2016/11/2.
 * 播放音乐的工厂类
 */
public class MusicFactory {
    /**
     * 创建本地音乐播放
     * @param context 上下文
     * @return
     */
    public static IMusic produceLocalPlay(Context context) {
        return new LocalMusicPlayImpl(context);
    }

    /**
     * 创建喜马拉雅的音乐播放
     * @param context 上下文
     * @return
     */
    public static IMusic produceXiMaLaYaPlay(Context context) {
        return new XiMaLaYaImpl(context);
    }
}
