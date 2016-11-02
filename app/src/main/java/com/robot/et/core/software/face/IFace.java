package com.robot.et.core.software.face;

import com.robot.et.core.software.face.callback.FaceCallBack;
import com.robot.et.entity.FaceInfo;

import java.util.ArrayList;

/**
 * Created by houdeming on 2016/11/1.
 * 人脸识别的对外接口
 */
public interface IFace {
    /**
     * 打开人脸识别
     * @param isVoiceOpen 是否是语音开启
     * @param infos 人脸信息
     * @param callBack 结果回调
     */
    void openFaceDistinguish(boolean isVoiceOpen, ArrayList<FaceInfo> infos, FaceCallBack callBack);

    /**
     * 关闭人脸识别
     */
    void closeFaceDistinguish();
}