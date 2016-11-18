package com.robot.et.business.control;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.business.voice.callback.ListenResultCallBack;
import com.robot.et.business.voice.callback.SpeakEndCallBack;
import com.robot.et.business.voice.callback.VolumeCallBack;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.db.RobotDB;
import com.robot.et.entity.FamilyLocationInfo;
import com.slamtec.slamware.robot.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houdeming on 2016/11/18.
 * 安防
 */

public class Security {

    private static final String TAG = "security";
    private static boolean isVolumeOver = false;

    /**
     * 进入安保场景
     */
    public static void enterSecurity() {
        GlobalConfig.isSecurityMode = true;
        isVolumeOver = false;
        VoiceHandler.speak("好的，已进入安保模式", new SpeakEndCallBack() {
            @Override
            public void onSpeakEnd() {
                VoiceHandler.listen(new VolumeCallBack() {

                    @Override
                    public void onVolumeChanged(int volumeValue) {
                        Log.i(TAG, "volumeValue===" + volumeValue);
                        if (volumeValue > 10 && !isVolumeOver) {
                            isVolumeOver = true;
                            VoiceHandler.stopListen();
                            VoiceHandler.setVolumeCallBack(null);
                            Log.i(TAG, "开始安防");
                            if (GlobalConfig.isConnectSlam) {
                                patrol();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 退出安保场景
     */
    public static void outSecurity() {
        GlobalConfig.isSecurityMode = false;
        VoiceHandler.speakEndToListen("好的，已退出安保模式");
    }

    /**
     * 确认安保
     *
     * @param content        要说的内容
     * @param isSecurityMode 是否是安保模式
     */
    public static void confirmSecurity(String content, final boolean isSecurityMode) {
        VoiceHandler.speak(content, new SpeakEndCallBack() {
            @Override
            public void onSpeakEnd() {
                VoiceHandler.listen(new ListenResultCallBack() {
                    @Override
                    public void onListenResult(String result) {
                        if (result.contains("好的")) {
                            if (isSecurityMode) {
                                outSecurity();
                            } else {
                                enterSecurity();
                            }
                        } else {
                            if (isSecurityMode) {
                                confirmSecurity("请回答“好的”退出安保模式", isSecurityMode);
                            } else {
                                confirmSecurity("请回答“好的”进入安保模式", isSecurityMode);
                            }
                        }
                    }
                });

            }
        });
    }

    /**
     * 巡逻
     */
    private static void patrol() {
        RobotDB mDb = RobotDB.getInstance();
        List<FamilyLocationInfo> infos = mDb.getFamilyLocationInfos();
        int size = infos.size();
        Log.i(TAG, "size==" + size);
        if (infos != null && size > 0) {
            List<Location> locations = new ArrayList<Location>();
            for (int i = 0; i < size; i++) {
                Location location = new Location();
                String posX = infos.get(i).getPositionX();
                String posY = infos.get(i).getPositionY();
                if (!TextUtils.isEmpty(posX) && !TextUtils.isEmpty(posY)) {
                    location.setX(Float.parseFloat(posX));
                    location.setY(Float.parseFloat(posY));
                    locations.add(location);
                }
            }
            if (locations != null && locations.size() > 0) {
                SlamtecLoader.getInstance().execPatrol(locations);
            }
        }
    }
}
