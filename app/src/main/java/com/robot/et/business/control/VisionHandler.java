package com.robot.et.business.control;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.business.voice.callback.SpeakEndCallBack;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.vision.Vision;
import com.robot.et.core.software.vision.callback.LearnOpenCallBack;
import com.robot.et.core.software.vision.callback.LearnWaringCallBack;
import com.robot.et.core.software.vision.callback.VisionLearnCallBack;
import com.robot.et.core.software.vision.callback.VisionRecogniseCallBack;
import com.robot.et.util.MatchStringUtil;

/**
 * Created by houdeming on 2016/11/18.
 */

public class VisionHandler {

    private static final String TAG = "visionHandler";

    /**
     * 学习
     * @param result
     * @return
     */
    public static boolean visionLearn(String result) {
        if (!GlobalConfig.isConnectVision) {
            VoiceHandler.speakEndToListen("未连接视觉");
            return true;
        }
        // 跟随的时候不开启视觉学习
        if (GlobalConfig.isFollow) {
            return false;
        }
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
        return true;
    }

    /**
     * 跟随
     * @return
     */
    public static boolean visionFollow() {
        if (!GlobalConfig.isConnectVision) {
            VoiceHandler.speakEndToListen("未连接视觉");
            return true;
        }
        // 视觉学习的时候 或已经在跟随的话 不开启跟随
        if (GlobalConfig.isVisionLearn || GlobalConfig.isFollow) {
            return false;
        }
        VoiceHandler.speakEndToListen("好的");
        FollowBody.getInstance().follow();
        return true;
    }

    private static boolean isLearnIng = false;
    private static boolean isSpeakIng = false;
    private static int lastWaringId = 0;

    /**
     * 学习
     *
     * @param result
     */
    private static void learn(final String result) {
        isLearnIng = false;
        isSpeakIng = false;
        lastWaringId = 0;// 位置正好的时候是0
        // 每次学习或者识别的时候先获取学习中的提示
        Vision.getInstance().getLearnWaring(new LearnWaringCallBack() {
            @Override
            public void onLearnWaring(int id, String content) {
                Log.i(TAG, "id==" + id);
                if (id == 0) {// 位置是正确的
                    if (!isLearnIng) {
                        isLearnIng = true;
                        if (TextUtils.isEmpty(result)) {// 什么   识别
                            startRecognise();
                        } else {// 学习
                            startLearn(result);
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
    private static void startLearn(String content) {
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
     */
    private static void startRecognise() {
        VoiceHandler.speak("正在识别中，请等待", null);
        Vision.getInstance().startRecognise(new VisionRecogniseCallBack() {
            @Override
            public void onVisionRecogniseResult(boolean isRecogniseSuccess, String speakContent) {
                VoiceHandler.speakEndToListen(speakContent);
            }
        });
    }
}
