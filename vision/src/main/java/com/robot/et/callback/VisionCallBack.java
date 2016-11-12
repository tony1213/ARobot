package com.robot.et.callback;

/**
 * Created by houdeming on 2016/11/11.
 * 视觉学习的回调
 */

public interface VisionCallBack {
    /**
     * 视觉学习的摄像头打开结束
     */
    void learnOpenEnd();

    /**
     * 视觉学习发出的警告
     *
     * @param id 警告id
     */
    void learnWaring(int id);

    /**
     * 学习结束
     */
    void learnEnd();

    /**
     * 学习识别结束
     *
     * @param name 物体名字
     * @param conf 物体的识别度
     */
    void learnRecogniseEnd(String name, int conf);

    /**
     * 人体的位置
     *
     * @param centerX X坐标
     * @param centerY Y坐标
     * @param centerZ Z坐标
     */
    void bodyPosition(float centerX, float centerY, float centerZ);
}
