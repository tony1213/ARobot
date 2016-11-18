package com.robot.et.business.vision.callback;

/**
 * Created by houdeming on 2016/11/18.
 */

public interface VisionImgInfoCallBack {
    /**
     * 获取视觉图像的信息
     * @param width 宽
     * @param height 高
     * @param dataType 格式
     */
    void onVisionImgInfo(int width, int height, int dataType);
}
