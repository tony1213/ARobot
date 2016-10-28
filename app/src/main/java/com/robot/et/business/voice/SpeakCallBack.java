package com.robot.et.business.voice;

/**
 * Created by houdeming on 2016/10/28.
 */
public interface SpeakCallBack {
    /**
     * 说话结束
     *
     * @param isSpeakSuccess 说话是否成功
     */
    void onSpeakEnd(boolean isSpeakSuccess);
}
