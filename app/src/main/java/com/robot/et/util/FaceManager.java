package com.robot.et.util;

import com.robot.et.db.RobotDB;
import com.robot.et.entity.FaceInfo;

/**
 * 人脸识别的管理
 */
public class FaceManager {
    /**
     * 增加人脸识别的信息
     *
     * @param faceName 对应的人脸的名字
     * @param registerId 注册的ID
     */
    public static void addFaceInfo(String faceName, String registerId) {
        FaceInfo info = new FaceInfo();
        info.setAuthorId(registerId);
        info.setAuthorName(faceName);
        RobotDB.getInstance().addFaceInfo(info);
    }
}
