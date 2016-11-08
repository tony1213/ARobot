package com.robot.et.core.software.camera;

import android.content.Context;

import com.robot.et.core.software.camera.impl.local.LocalCameraImpl;

/**
 * Created by houdeming on 2016/11/2.
 * 照相机的工厂类
 */
public class CameraFactory {
    /**
     * 创建本地照相机
     * @param context 上下文
     * @return
     */
    public static ICamera produceLocalCamera(Context context) {
        return new LocalCameraImpl(context);
    }
}
