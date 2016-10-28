package com.robot.et.core.software.voice.turing;

/**
 * Created by houdeming on 2016/9/5.
 * 图灵接口
 */
public interface ITuring {
    // 图灵文本理解结果
    void onTuringResult(String result);

    // 图灵文本理解错误
    void onTuringError(String errorMsg);
}
