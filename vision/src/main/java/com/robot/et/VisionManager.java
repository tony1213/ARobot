package com.robot.et;

import android.os.RemoteException;

import com.robot.et.callback.VisionCallBack;
import com.robot.et.core.hardware.vision.RobotVision;
import com.robot.et.vision.IRobotVision;

/**
 * Created by houdeming on 2016/11/12.
 * 视觉对外提供的调用
 */

public class VisionManager extends IRobotVision.Stub {

    private VisionCallBack mCallBack;

    public VisionManager(VisionCallBack callBack) {
        mCallBack = callBack;
        RobotVision.setVisionCallBack(callBack);
    }

    @Override
    public int visionInit() throws RemoteException {
        return RobotVision.visionInit();
    }

    @Override
    public void visionUninit() throws RemoteException {
        RobotVision.visionUninit();
    }

    @Override
    public void visionLearnOpen() throws RemoteException {
        RobotVision.objLearnOpen();
    }

    @Override
    public void visionLearnClose() throws RemoteException {
        RobotVision.objLearnClose();
    }

    @Override
    public void objLearnStartLearn(String str) throws RemoteException {
        RobotVision.objLearnStartLearn(str);
    }

    @Override
    public void objLearnStartRecog() throws RemoteException {
        RobotVision.objLearnStartRecog();
    }

    @Override
    public void testCallback() throws RemoteException {
        RobotVision.testCallback();
    }

    @Override
    public void bodyDetectOpen() throws RemoteException {
        RobotVision.bodyDetectOpen();
    }

    @Override
    public void bodyDetectClose() throws RemoteException {
        RobotVision.bodyDetectClose();
    }

    @Override
    public void bodyDetectGetPos() throws RemoteException {
        RobotVision.Postion3Df pos = new RobotVision.Postion3Df();
        RobotVision.bodyDetectGetPos(pos);
        if (mCallBack != null) {
            mCallBack.bodyPosition(pos.centerX, pos.centerY, pos.centerZ);
        }
    }
}
