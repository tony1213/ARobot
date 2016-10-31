package com.robot.et.core.software.music;

import com.robot.et.core.software.music.callback.MusicCallBack;

/**
 * Created by houdeming on 2016/10/31.
 * 对外音乐播放接口
 */
public interface IMusic {
    /**
     * 播放
     *
     * @param playType 播放类型
     * @param content  播放内容
     * @param callBack 播放器的回调
     */
    void play(int playType, String content, MusicCallBack callBack);

    // 停止播放
    void stopPlay();

    // 释放播放器
    void destroyPlayer();
}
