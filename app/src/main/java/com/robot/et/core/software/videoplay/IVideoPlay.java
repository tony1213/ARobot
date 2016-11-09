package com.robot.et.core.software.videoplay;

import com.robot.et.core.software.videoplay.callback.VideoPlayCallBack;

/**
 * Created by houdeming on 2016/10/31.
 * 对外视频播放接口
 */
public interface IVideoPlay {
    /**
     * 播放
     *
     * @param content  播放内容
     * @param callBack 播放器的回调
     */
    void play(String content, VideoPlayCallBack callBack);

    /**
     * 停止播放
     */
    void stopPlay();

    /**
     * 关闭视频
     */
    void closeVideo();

    /**
     * 是否在播放视频
     * @return
     */
    boolean isPlayVideo();
}
