package com.robot.et.business.voice;

/**
 * Created by houdeming on 2016/8/8.
 */
public interface ISpeech {
    /**开始语音合成
     *
     * @param speakContent 要说的话
     * @param callBack 说话结果的回调
     */
    void startSpeak(String speakContent, SpeakCallBack callBack);

    // 取消语音合成
    void cancelSpeak();

    /**开始语音听写
     *
     * @param callBack 听写结果的回调
     */
    void startListen(ListenCallBack callBack);

    // 取消语音听写
    void cancelListen();

    /**理解文本
     *
     * @param content 要理解的内容
     * @param callBack 理解结果的回调
     */
    void understanderText(String content, UnderstandCallBack callBack);
}
