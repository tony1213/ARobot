package com.robot.et.core.software.system;

import android.content.Context;
import android.media.AudioManager;

import com.robot.et.app.CustomApplication;

// 调节系统音量大小
public class VolumeControlManager {
    private static AudioManager mAudioManager;

    static {
        mAudioManager = (AudioManager) CustomApplication.getInstance().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    // 增加音量
    public static void increaseVolume() {
        if (getCurrentVolume() < getMaxVolume()) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }

    // 降低音量
    public static void reduceVolume() {
        if (getCurrentVolume() > 0) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }

    // 获取最大音量值
    public static int getMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    // 获取当前音量值
    public static int getCurrentVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /** 设置当前音量
     *
     * @param volumeValue 音量值
     */
    public static void setCurrentVolume(int volumeValue) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeValue, 0);
    }

    // 设置最大音量
    public static void setMaxVolume() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getMaxVolume(), 0);
    }
}
