package com.robot.et.util;

import com.robot.et.db.RobotDB;
import com.robot.et.entity.FaceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸识别的管理
 */
public class FaceManager {
    private static List<FaceInfo> infos = new ArrayList<FaceInfo>();// 人脸的信息
    private static String authorId;// 人脸注册的id
    private static String authorName;// 人脸注册的名字

    /**
     * 设置新的脸部数据
     * @param faceInfos 脸部数据
     */
    public static void setNewFaceInfo(List<FaceInfo> faceInfos) {
        int size = faceInfos.size();
        if (faceInfos != null && size > 0) {
            faceInfos.remove(0);
            infos = faceInfos;
        } else {
            infos = null;
        }
    }

    public static List<FaceInfo> getFaceInfos() {
        return infos;
    }

    /**
     * 增加人脸识别的信息
     * @param faceName 对应的人脸的名字
     */
    public static void addFaceInfo(String faceName) {
        FaceInfo info = new FaceInfo();
        info.setAuthorId(getAuthorId());
        info.setAuthorName(faceName);
        RobotDB.getInstance().addFaceInfo(info);
    }

    public static String getAuthorId() {
        return authorId;
    }

    public static void setAuthorId(String authorId) {
        FaceManager.authorId = authorId;
    }

    public static String getAuthorName() {
        return authorName;
    }

    public static void setAuthorName(String authorName) {
        FaceManager.authorName = authorName;
    }
}
