package com.robot.et.business.control;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.control.orderenum.MatchSceneEnum;
import com.robot.et.business.vision.Vision;
import com.robot.et.business.vision.callback.LearnOpenCallBack;
import com.robot.et.business.vision.callback.LearnWaringCallBack;
import com.robot.et.business.vision.callback.VisionLearnCallBack;
import com.robot.et.business.vision.callback.VisionRecogniseCallBack;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.business.voice.callback.ListenResultCallBack;
import com.robot.et.business.voice.callback.SpeakEndCallBack;
import com.robot.et.business.voice.callback.VolumeCallBack;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.core.software.system.volume.VolumeControlManager;
import com.robot.et.db.RobotDB;
import com.robot.et.entity.FamilyLocationInfo;
import com.robot.et.util.MatchStringUtil;
import com.slamtec.slamware.robot.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houdeming on 2016/9/9.
 * 场景匹配处理
 */
public class MatchScene {

    private static final String TAG = "scene";

    /**
     * 匹配场景
     *
     * @param context Android上下文
     * @param result  匹配的內容
     * @return
     */
    public static boolean isMatchScene(Context context, String result) {
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        // 获取场景的Enum
        MatchSceneEnum sceneEnum = getScene(result);
        Log.i(TAG, "sceneEnum=====" + sceneEnum);
        // 如果场景为null的话，获取机器人的学习内容
        if (sceneEnum == null) {
            return false;
        }
        boolean flag = false;
        switch (sceneEnum) {
            case VOICE_BIGGEST_SCENE:// 声音最大
                flag = true;
                VolumeControlManager.setMaxVolume();
                VoiceHandler.speakEndToListen("音量已经最大");

                break;
            case VOICE_LITTEST_SCENE:// 声音最小
                flag = true;
                VolumeControlManager.setCurrentVolume(6);
                VoiceHandler.speakEndToListen("音量已经最小");

                break;
            case VOICE_BIGGER_INDIRECT_SCENE:// 间接增加声音
                flag = true;
                VolumeControlManager.increaseVolume();
                VoiceHandler.speakEndToListen("音量增加");

                break;
            case VOICE_LITTER_INDIRECT_SCENE:// 间接降低声音
                flag = true;
                VolumeControlManager.reduceVolume();
                VoiceHandler.speakEndToListen("音量减小");

                break;
            case VOICE_BIGGER_SCENE:// 直接增加声音
                flag = true;
                VolumeControlManager.increaseVolume();
                VoiceHandler.speakEndToListen("音量增加");

                break;
            case VOICE_LITTER_SCENE:// 直接降低声音
                flag = true;
                VolumeControlManager.reduceVolume();
                VoiceHandler.speakEndToListen("音量减小");

                break;
            case QUESTION_ANSWER_SCENE:// 智能学习回答话语

                break;
            case DISTURB_OPEN_SCENE:// 免打扰开

                break;
            case DISTURB_CLOSE_SCENE:// 免打扰关

                break;
            case SHUT_UP_SCENE:// 休息

                break;
            case DO_ACTION_SCENE:// 智能学习做动作

                break;
            case RAISE_LEFT_HAND_SCENE:// 抬左手

                break;
            case RAISE_RIGHT_HAND_SCENE:// 抬右手

                break;
            case RAISE_HAND_SCENE:// 抬手

                break;
            case WAVING_SCENE:// 摆手

                break;
            case HEAD_UP_SCENE:// 抬头

                break;
            case HEAD_DOWN_SCENE:// 低头

                break;
            case PLAY_SCRIPT_SCENE:// 表演节目

                break;
            case DANCE_SCENE:// 跳舞

                break;
            case FACE_NAME_SCENE:// 脸部名称

                break;
            case FACE_TEST_SCENE:// 开启脸部识别

                break;
            case LOOK_PHOTO_SCENE:// 看看照片的标志

                break;
            case ROBOT_NUM_SCENE:// 机器人编号的标志

                break;
            case STORY_SCENE:// 讲故事的场景

                break;
            case PLAY_TRAILER_SCENE:// 播放宣传片

                break;
            case OPEN_SECURITY_SCENE:// 进入安保场景
                flag = true;
                enterSecurity();

                break;
            case CLOSE_SECURITY_SCENE:// 退出安保场景
                flag = true;
                outSecurity();

                break;
            case CONFIRM_SECURITY_SCENE:// 确认安保场景的标志
                flag = true;
                if (GlobalConfig.isSecurityMode) {// 已经在安保模式
                    security("已经在安保模式了，要退出来吗？", true);
                } else {// 不在安保模式
                    security("要进入安保模式吗？", false);
                }

                break;
            case PHOTOGRAPH_SCENE:// 拍照

                break;
            case ENVIRONMENT_LEARN_SCENE:// 环境认识学习
                if (!GlobalConfig.isConnectSlam) {
                    VoiceHandler.speakEndToListen("未连接底盘");
                    return true;
                }
                String locationName = MatchStringUtil.getLocationName(result);
                Log.i(TAG, "locationName===" + locationName);
                if (!TextUtils.isEmpty(locationName)) {
                    flag = true;
                    rememberLocation(locationName);
                }

                break;
            case VISION_LEARN_SCENE:// 视觉学习
                if (!GlobalConfig.isConnectVision) {
                    VoiceHandler.speakEndToListen("未连接视觉");
                    return true;
                }
                // 跟随的时候不开启视觉学习
                if (GlobalConfig.isFollow) {
                    return false;
                }
                flag = true;
                final String learnContent = MatchStringUtil.getVisionLearnAnswer(result);
                // 视觉学习只打开一次，不连续打开
                if (!GlobalConfig.isVisionLearn) {
                    GlobalConfig.isVisionLearn = true;
                    Vision.getInstance().openLearn(new LearnOpenCallBack() {
                        @Override
                        public void onLearnOpenEnd() {
                            learn(learnContent);
                        }
                    });
                } else {
                    learn(learnContent);
                }

                break;
            case GO_WHERE_SCENE:// 去哪里的指令
                if (!GlobalConfig.isConnectSlam) {
                    VoiceHandler.speakEndToListen("未连接底盘");
                    return true;
                }
                String areaName = MatchStringUtil.getGoWhereAnswer(result);
                Log.i(TAG, "areaName===" + areaName);
                if (!TextUtils.isEmpty(areaName)) {
                    if (areaName.length() < 8) {
                        flag = true;
                        VoiceHandler.speakEndToListen("好的");
                        goToLocation(areaName);
                    }
                }

                break;
            case OPEN_MOTION_SCENE:// 打开运动

                break;
            case CLOSE_MOTION_SCENE:// 关闭运动

                break;
            case FORGET_LEARN_SCENE:// 忘记学习内容

                break;
            case ROAM_SCENE:// 漫游

                break;
            case FOLLOW_SCENE:// 跟着我
                flag = true;
                if (!GlobalConfig.isConnectVision) {
                    VoiceHandler.speakEndToListen("未连接视觉");
                    return true;
                }
                // 视觉学习的时候不开启跟随
                if (GlobalConfig.isVisionLearn) {
                    return false;
                }
                follow();

                break;
        }
        return flag;
    }

    /**
     * 获取场景的enum
     *
     * @param str
     * @return
     */
    private static MatchSceneEnum getScene(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (MatchSceneEnum sceneEnum : MatchSceneEnum.values()) {
                if (sceneEnum.isScene(str)) {
                    return sceneEnum;
                }
            }
        }
        return null;
    }

    /**
     * 安保
     *
     * @param content        要说的内容
     * @param isSecurityMode 是否是安保模式
     */
    private static void security(String content, final boolean isSecurityMode) {
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
                                security("请回答“好的”退出安保模式", isSecurityMode);
                            } else {
                                security("请回答“好的”进入安保模式", isSecurityMode);
                            }
                        }
                    }
                });

            }
        });
    }

    private static boolean isVolumeOver = false;

    /**
     * 进入安保场景
     */
    private static void enterSecurity() {
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
    private static void outSecurity() {
        GlobalConfig.isSecurityMode = false;
        VoiceHandler.speakEndToListen("好的，已退出安保模式");
    }

    /**
     * 跟随
     */
    private static void follow() {
        VoiceHandler.speakEndToListen("好的");
        FollowBody.getInstance().follow();
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

    /**
     * 记住环境位置
     *
     * @param locationName 位置
     */
    private static void rememberLocation(String locationName) {
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
        VoiceHandler.speakEndToListen("好的，我记住了");
    }

    /**
     * 去哪里
     *
     * @param locationName
     */
    private static void goToLocation(String locationName) {
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

    private static boolean isLearnIng = false;
    private static boolean isSpeakIng = false;
    private static int lastWaringId = 0;

    /**
     * 学习
     *
     * @param content
     */
    private static void learn(String content) {
        isLearnIng = false;
        isSpeakIng = false;
        lastWaringId = 0;
        // 每次学习或者识别的时候先获取学习中的提示
        Vision.getInstance().getLearnWaring(new LearnWaringCallBack() {
            @Override
            public void onLearnWaring(int id, String content) {
                Log.i(TAG, "id==" + id);
                if (id == 0) {// 位置是正确的
                    if (!isLearnIng) {
                        isLearnIng = true;
                        if (TextUtils.isEmpty(content)) {// 什么   识别
                            visionRecognise(content);
                        } else {// 学习
                            visionLearn(content);
                        }
                    } else {
                        speak(id, content);
                    }
                } else {// 位置比较偏
                    speak(id, content);
                }
            }
        });
    }

    private static void speak(int id, String content) {
        if (!isSpeakIng && lastWaringId != id) {
            isSpeakIng = true;
            lastWaringId = id;
            VoiceHandler.speak(content, new SpeakEndCallBack() {
                @Override
                public void onSpeakEnd() {
                    isSpeakIng = false;
                }
            });
        }
    }

    /**
     * 视觉学习
     *
     * @param content
     */
    private static void visionLearn(String content) {
        VoiceHandler.speak("请不同角度展示物体", null);
        Vision.getInstance().startLearn(content, new VisionLearnCallBack() {
            @Override
            public void onLearnEnd() {
                VoiceHandler.speakEndToListen("好的，我记住了");
            }
        });
    }

    /**
     * 视觉识别
     *
     * @param content
     */
    private static void visionRecognise(final String content) {
        VoiceHandler.speak("正在识别中，请等待", null);
        Vision.getInstance().startRecognise(new VisionRecogniseCallBack() {
            @Override
            public void onVisionRecogniseResult(boolean isRecogniseSuccess, String speakContent) {
                VoiceHandler.speakEndToListen(speakContent);
            }
        });
    }
}
