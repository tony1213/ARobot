package com.robot.et.business.voice.callback;

/**
 * Created by houdeming on 2016/11/15.
 * 音量的回调
 */

public interface VolumeCallBack {
    /**
     * 音量值的回调
     * @param volumeValue
     */
    void onVolumeChanged(int volumeValue);
}
