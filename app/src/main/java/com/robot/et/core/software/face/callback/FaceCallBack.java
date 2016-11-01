package com.robot.et.core.software.face.callback;

/**
 * Created by houdeming on 2016/11/1.
 * 脸部识别的结果
 */
public interface FaceCallBack {
    /**
     * 脸部识别的结果
     * @param isDistinguishSuccess 识别是否成功
     * @param content 返回的内容
     */
    void onFaceResult(boolean isDistinguishSuccess, String content);

    /**
     * 人脸识别异常
     */
    void onFaceError();
}
