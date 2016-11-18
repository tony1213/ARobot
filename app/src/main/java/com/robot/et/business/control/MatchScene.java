package com.robot.et.business.control;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.control.orderenum.MatchSceneEnum;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.system.volume.VolumeControlManager;

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
                Security.enterSecurity();

                break;
            case CLOSE_SECURITY_SCENE:// 退出安保场景
                flag = true;
                Security.outSecurity();

                break;
            case CONFIRM_SECURITY_SCENE:// 确认安保场景的标志
                flag = true;
                if (GlobalConfig.isSecurityMode) {// 已经在安保模式
                    Security.confirmSecurity("已经在安保模式了，要退出来吗？", true);
                } else {// 不在安保模式
                    Security.confirmSecurity("要进入安保模式吗？", false);
                }

                break;
            case PHOTOGRAPH_SCENE:// 拍照

                break;
            case ENVIRONMENT_LEARN_SCENE:// 环境认识学习
                flag = Navigation.rememberLocation(result);

                break;
            case VISION_LEARN_SCENE:// 视觉学习
                flag = VisionHandler.visionLearn(result);

                break;
            case GO_WHERE_SCENE:// 去哪里的指令
                flag = Navigation.goDesignedLocation(result);

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
                flag = VisionHandler.visionFollow();

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
}
