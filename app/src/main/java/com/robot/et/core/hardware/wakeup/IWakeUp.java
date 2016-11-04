package com.robot.et.core.hardware.wakeup;

/**
 * 唤醒的接口
 */
public interface IWakeUp {
    /**
     * 获取语音唤醒角度
     * @param degree 唤醒角度
     */
    void getVoiceWakeUpDegree(int degree);

    /**
     * 人体检测
     */
    void bodyDetection();

    /**
     * 触摸
     * @param touchId 触摸Id
     */
    void bodyTouch(int touchId);

    /**
     * 短按
     */
    void shortPress();
}
