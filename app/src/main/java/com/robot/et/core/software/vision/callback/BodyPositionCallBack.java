package com.robot.et.core.software.vision.callback;

/**
 * Created by houdeming on 2016/11/14.
 * 人体位置的回调
 *
 */

public interface BodyPositionCallBack {
    /**
     * 人体的位置
     *
     * @param centerX X坐标
     * @param centerY Y坐标
     * @param centerZ Z坐标
     */
    void onBodyPosition(float centerX, float centerY, float centerZ);
}
