package com.robot.et.core.software.voice.ifly;

/**
 * Created by houdeming on 2016/9/5.
 * 文本理解接口
 */
public interface ITextUnderstand {
    // 文本理解结果
    void onUnderstandResult(String result);

    // 文本理解错误
    void onUnderstandError(int errorCode);
}
