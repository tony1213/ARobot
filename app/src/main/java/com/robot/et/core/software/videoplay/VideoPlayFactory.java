package com.robot.et.core.software.videoplay;

import android.content.Context;

import com.robot.et.core.software.videoplay.impl.local.LocalVideoPlayImpl;

/**
 * Created by houdeming on 2016/11/2.
 * 播放视频的工厂类
 */
public class VideoPlayFactory {
    /**
     * 创建本地视频播放
     *
     * @param context 上下文
     * @return
     */
    public static IVideoPlay produceLocalPlay(Context context) {
        return new LocalVideoPlayImpl(context);
    }
}
