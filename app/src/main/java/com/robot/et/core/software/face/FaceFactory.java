package com.robot.et.core.software.face;

import android.content.Context;

import com.robot.et.core.software.face.impl.iflytek.IflyFaceImpl;

/**
 * Created by houdeming on 2016/11/2.
 * 人脸识别的工厂类
 */
public class FaceFactory {
    /**
     * 创建科大讯飞的人脸识别
     *
     * @param context 上下文
     * @return
     */
    public static IFace produceIflyFace(Context context) {
        return new IflyFaceImpl(context);
    }
}
