package com.robot.et.core.software.voice.callback;

/**
 * Created by houdeming on 2016/10/28.
 */
public interface ListenCallBack {
    // 开始语音听写
    void onListenBegin();

    // 语音听写结束
    void onListenEnd();

    /**
     * 获取语音听写时说话音量的变化值
     * @param volumeValue 音量的变化值
     */
    void onVolumeChanged(int volumeValue);

    /**
     * 听的结果回调
     *
     * @param result 听写的结果
     */
    void onListenResult(String result);
}
