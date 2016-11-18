// IRobotVision.aidl
package com.robot.et.vision;

interface IRobotVision {

    // 初始化视觉
    int visionInit();

    // 视觉反初始化
    void visionUninit();

    // 打开学习
    void visionLearnOpen();

    // 关闭学习
    void visionLearnClose();

    // 开始学习
    void objLearnStartLearn(String str);

    // 开始识别
    void objLearnStartRecog();

    // 测试回调
    void testCallback();

    // 打开人体检测
    void bodyDetectOpen();

    // 关闭人体检测
    void bodyDetectClose();

    // 人体检测
    void bodyDetectGetPos();

    // 获取视觉图片的信息
    void getVisionImgInfo();

    // 获取视觉图片的bitmap
    void getVisionImgBitmap();
}
