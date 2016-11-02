package com.robot.et.core.software.videoplay;

import android.content.Context;

/**
 * Created by houdeming on 2016/11/2.
 * 视频播放工厂接口
 */
public interface VideoPlayFactory {
    /**
     * 创建视频播放
     *
     * @param context 上下文
     * @return
     */
    IVideoPlay createVideoPlay(Context context);
}
