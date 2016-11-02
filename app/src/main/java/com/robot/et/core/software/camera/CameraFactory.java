package com.robot.et.core.software.camera;

import android.content.Context;

/**
 * Created by houdeming on 2016/11/2.
 * 创建照相机的工厂接口
 */
public interface CameraFactory {
    /**
     * 创建照相机
     * @param context 上下文
     * @return
     */
    ICamera createCamera(Context context);
}
