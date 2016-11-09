package com.robot.et.core.software.voice;

import com.robot.et.core.software.voice.callback.ListenCallBack;
import com.robot.et.core.software.voice.callback.SpeakCallBack;
import com.robot.et.core.software.voice.callback.UnderstandCallBack;

/**
 * Created by houdeming on 2016/10/29.
 * 对外语音接口
 */
public interface IVoice {
    /**开始语音合成
     *
     * @param speakContent 要说的话
     * @param callBack 说话结果的回调
     */
    void startSpeak(String speakContent, SpeakCallBack callBack);

    // 停止语音合成
    void stopSpeak();

    /**开始语音听写
     *
     * @param callBack 听写结果的回调
     */
    void startListen(ListenCallBack callBack);

    // 停止语音听写
    void stopListen();

    /**理解文本
     *
     * @param content 要理解的内容
     * @param callBack 理解结果的回调
     */
    void understanderText(String content, UnderstandCallBack callBack);

    // 销毁语音
    void destroyVoice();
}
