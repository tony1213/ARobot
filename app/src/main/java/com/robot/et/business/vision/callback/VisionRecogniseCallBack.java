package com.robot.et.business.vision.callback;

/**
 * Created by houdeming on 2016/11/14.
 * 视觉识别的回调
 */

public interface VisionRecogniseCallBack {
    /**
     * 识别结果
     *
     * @param name 物体名字
     * @param conf 物体的识别度
     */
    void onVisionRecogniseResult(String name, int conf);
}
