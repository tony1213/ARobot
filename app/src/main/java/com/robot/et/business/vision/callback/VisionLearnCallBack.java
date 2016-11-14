package com.robot.et.business.vision.callback;

/**
 * Created by houdeming on 2016/11/14.
 * 视觉学习的回调
 */

public interface VisionLearnCallBack {
    /**
     * 学习打开
     */
    void onLearnOpenEnd();

    /**
     * 学习中的警告
     * @param content
     */
    void onLearnWaring(String content);

    /**
     * 学习结束
     */
    void onLearnEnd();
}
