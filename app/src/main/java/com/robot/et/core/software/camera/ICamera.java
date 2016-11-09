package com.robot.et.core.software.camera;

import com.robot.et.core.software.camera.callback.CameraCallBack;

/**
 * Created by houdeming on 2016/10/31.
 * 对外拍照接口
 */
public interface ICamera {
    // 拍照
    void takePhoto(CameraCallBack callBack);

    // 关闭相机
    void closeCamera();
}
