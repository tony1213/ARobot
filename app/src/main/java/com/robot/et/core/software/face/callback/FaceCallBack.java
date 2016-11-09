package com.robot.et.core.software.face.callback;

/**
 * Created by houdeming on 2016/11/1.
 * 脸部识别的结果
 */
public interface FaceCallBack {
    /**
     * 脸部识别
     * @param isDistinguishSuccess 识别是否成功
     * @param faceName 人脸对应的名字
     */
    void onFaceDistinguish(boolean isDistinguishSuccess, String faceName);

    /**
     * 脸部注册
     * @param isRegisterSuccess 是否注册成功
     * @param registerId 注册的ID
     */
    void onFaceRegister(boolean isRegisterSuccess, String registerId);

    /**
     * 脸部的位置
     * @param facePointX 脸部x坐标
     * @param facePointY 脸部y坐标
     */
    void onFacePoint(float facePointX, float facePointY);

    /**
     * 人脸识别异常
     */
    void onFaceError();
}
