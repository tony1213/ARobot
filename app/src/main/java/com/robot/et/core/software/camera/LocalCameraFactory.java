package com.robot.et.core.software.camera;

import android.content.Context;

import com.robot.et.core.software.camera.impl.local.LocalCameraImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class LocalCameraFactory implements CameraFactory {
    @Override
    public ICamera createCamera(Context context) {
        return new LocalCameraImpl(context);
    }
}
