// IRobotVision.aidl
package com.robot.et.vision;

interface IRobotVision {

    // 初始化视觉
    int visionInit();

    // 视觉反初始化
    void visionUninit();

    // 开始学习
    void objLearnStartLearn(String str);

    // 开始识别
    void objLearnStartRecog();

    // 测试回调
    void testCallback();

    // 人体检测
    void bodyDetectGetPos();
}
