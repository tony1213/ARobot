package com.robot.et.core.software.camera.callback;

/**
 * Created by houdeming on 2016/10/31.
 */
public interface CameraCallBack {
    // 拍照的结果数据
    void onCameraResult(byte[] data);

    // 失败
    void onFail();
}
