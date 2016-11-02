package com.robot.et.core.software.face;

import android.content.Context;

import com.robot.et.core.software.face.impl.iflytek.IflyFaceImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class IflyFaceFactory implements FaceFactory {
    @Override
    public IFace createFace(Context context) {
        return new IflyFaceImpl(context);
    }
}
