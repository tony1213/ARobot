package com.robot.et.core.software.camera.impl.local;

import android.content.Context;
import android.content.Intent;

import com.robot.et.core.software.camera.ICamera;
import com.robot.et.core.software.camera.callback.CameraCallBack;

/**
 * Created by houdeming on 2016/10/31.
 */
public class LocalCameraImpl implements ICamera {
    private Context context;
    private static CameraCallBack callBack;

    public LocalCameraImpl(Context context) {
        this.context = context;
    }

    protected static CameraCallBack getCallBack() {
        return callBack;
    }

    @Override
    public void takePhoto(CameraCallBack callBack) {
        this.callBack = callBack;
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void closeCamera() {
        if (TakePhotoActivity.instance != null) {
            TakePhotoActivity.instance.finish();
            TakePhotoActivity.instance = null;
        }
    }
}
