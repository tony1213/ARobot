package com.robot.et.business.vision.callback;

/**
 * Created by houdeming on 2016/11/14.
 * 打开学习的回调
 */

public interface LearnOpenCallBack {
    /**
     * 学习打开
     */
    void onLearnOpenEnd();

    /**
     * 学习中的警告
     *
     * @param id
     * @param content
     */
    void onLearnWaring(int id, String content);
}
