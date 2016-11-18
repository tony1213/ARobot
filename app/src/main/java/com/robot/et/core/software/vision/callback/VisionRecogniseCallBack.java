package com.robot.et.core.software.vision.callback;

/**
 * Created by houdeming on 2016/11/14.
 * 视觉识别的回调
 */

public interface VisionRecogniseCallBack {
    /**
     * 识别结果
     *
     * @param isRecogniseSuccess 是否识别成功
     * @param speakContent 说话的内容
     */
    void onVisionRecogniseResult(boolean isRecogniseSuccess, String speakContent);
}
