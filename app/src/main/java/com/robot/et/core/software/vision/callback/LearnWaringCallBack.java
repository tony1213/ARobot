package com.robot.et.core.software.vision.callback;

/**
 * Created by houdeming on 2016/11/15.
 * 视觉中的警告回调
 */

public interface LearnWaringCallBack {
    /**
     * 学习中的警告
     *
     * @param id
     * @param content
     */
    void onLearnWaring(int id, String content);
}
