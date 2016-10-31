package com.robot.et.core.software.music.callback;

/**
 * Created by houdeming on 2016/10/31.
 */
public interface MusicCallBack {
    // 开始播放
    void playStart();

    // 播放完成
    void playComplected();

    // 播放失败
    void playFail();
}
