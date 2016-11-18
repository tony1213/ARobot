package com.robot.et.business.control;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.db.RobotDB;
import com.robot.et.entity.FamilyLocationInfo;
import com.robot.et.util.MatchStringUtil;
import com.slamtec.slamware.robot.Location;

/**
 * Created by houdeming on 2016/11/18.
 * 导航
 */

public class Navigation {

    private static final String TAG = "navigation";

    /**
     * 记住位置
     * @param result
     * @return
     */
    public static boolean rememberLocation(String result) {
        if (!GlobalConfig.isConnectSlam) {
            VoiceHandler.speakEndToListen("未连接底盘");
            return true;
        }
        String locationName = MatchStringUtil.getLocationName(result);
        Log.i(TAG, "locationName===" + locationName);
        if (!TextUtils.isEmpty(locationName)) {
            addLocationToDB(locationName);
            VoiceHandler.speakEndToListen("好的，我记住了");
            return true;
        }
        return false;
    }

    /**
     * 去指定的位置
     * @param result
     * @return
     */
    public static boolean goDesignedLocation(String result) {
        if (!GlobalConfig.isConnectSlam) {
            VoiceHandler.speakEndToListen("未连接底盘");
            return true;
        }
        String areaName = MatchStringUtil.getGoWhereAnswer(result);
        Log.i(TAG, "areaName===" + areaName);
        if (!TextUtils.isEmpty(areaName)) {
            if (areaName.length() < 8) {
                VoiceHandler.speakEndToListen("好的");
                toLocation(areaName);
                return true;
            }
        }
        return false;
    }

    /**
     * 把位置信息添加到
     *
     * @param locationName 位置
     */
    private static void addLocationToDB(String locationName) {
        RobotDB mDb = RobotDB.getInstance();
        Location location = SlamtecLoader.getInstance().getCurrentRobotPose();
        String posX = String.valueOf(location.getX());
        String posY = String.valueOf(location.getY());
        Log.i(TAG, "posX===" + posX + "--posY==" + posY);
        FamilyLocationInfo info = mDb.getFamilyLocationInfo(locationName);
        if (info != null) {// 该位置已经记录，更新位置
            Log.i(TAG, "更新位置");
            mDb.updateFamilyLocationXY(locationName, posX, posY);
        } else {// 第一次记录该位置
            Log.i(TAG, "添加位置");
            FamilyLocationInfo mInfo = new FamilyLocationInfo();
            mInfo.setPositionName(locationName);
            mInfo.setPositionX(posX);
            mInfo.setPositionY(posY);
            mDb.addFamilyLocation(mInfo);
        }
    }

    /**
     * 去哪里
     *
     * @param locationName
     */
    private static void toLocation(String locationName) {
        FamilyLocationInfo info = RobotDB.getInstance().getFamilyLocationInfo(locationName);
        if (info != null) {
            String posX = info.getPositionX();
            String posY = info.getPositionY();
            Log.i(TAG, "posX===" + posX + "--posY==" + posY);
            if (!TextUtils.isEmpty(posX) && !TextUtils.isEmpty(posY)) {
                SlamtecLoader.getInstance().execSetGoal(Float.parseFloat(posX), Float.parseFloat(posY));
            } else {
                VoiceHandler.speakEndToListen("位置不明确，请先指定位置");
            }
        } else {
            VoiceHandler.speakEndToListen("位置不明确，请先指定位置");
        }
    }
}
