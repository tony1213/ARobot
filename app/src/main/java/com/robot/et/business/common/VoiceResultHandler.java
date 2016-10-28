package com.robot.et.business.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.voice.SceneServiceEnum;
import com.robot.et.business.voice.SpeakCallBack;
import com.robot.et.business.voice.SpeechImpl;
import com.robot.et.business.voice.UnderstandCallBack;

/**
 * Created by houdeming on 2016/10/28.
 * 语音结果的统一处理
 */
public class VoiceResultHandler {
    private static final String TAG = "listenHand";
    // 语音处理
    public static void handVoiceResult(Context context, String voiceResult) {
        Log.i(TAG, "voiceResult===" + voiceResult);
        if (!TextUtils.isEmpty(voiceResult)) {
            underStandVoiceResult(context, voiceResult);
        } else {
            SpeechImpl.getInstance().startListen(null);
        }
    }

    // 文本理解
    private static void underStandVoiceResult(final Context context, final String voiceResult) {
        SpeechImpl.getInstance().understanderText(voiceResult, new UnderstandCallBack() {
            @Override
            public void onResult(SceneServiceEnum serviceEnum, String understandResult) {
                Log.i(TAG, "serviceEnum===" + serviceEnum);
                Log.i(TAG, "understandResult===" + understandResult);
                if (!TextUtils.isEmpty(understandResult)) {
                    if (serviceEnum != null) {
                        switch (serviceEnum) {
                            case BAIKE://百科
                            case CALC://计算器
                            case COOKBOOK://菜谱
                            case DATETIME://日期
                            case FAQ://社区问答
                            case CHAT://闲聊
                            case OPENQA://褒贬&问候&情绪

                                speakContent(understandResult);

                                break;
                            case MUSIC://音乐
                                speakContent(understandResult);

                                break;
                            case SCHEDULE://提醒
                                // 日期 + 时间 + 说的日期 + 说的时间 + 做什么事
                                speakContent(understandResult);

                                break;
                            case WEATHER://天气查询
                                if (understandResult.contains("气温")) {
                                    speakContent(understandResult);
                                } else {// 因为不知道所在城市，沒有获取到天气重新获取
                                    underStandVoiceResult(context, understandResult);
                                }

                                break;
                            case PM25://空气质量
                                if (understandResult.contains("空气质量")) {
                                    speakContent(understandResult);
                                } else {// 因为不知道所在城市，沒有获取到空气质量重新获取
                                    underStandVoiceResult(context, understandResult);
                                }

                                break;

                            case TELEPHONE://打电话
                                speakContent(understandResult);

                                break;

                            case RADIO://电台
                                speakContent(understandResult);

                                break;
                        }
                    } else {
                        speakContent(understandResult);
                    }
                } else {
                    SpeechImpl.getInstance().startListen(null);
                }
            }
        });
    }

    // 说内容
    private static void speakContent(String content) {
        SpeechImpl.getInstance().startSpeak(content, new SpeakCallBack() {
            @Override
            public void onSpeakEnd(boolean isSpeakSuccess) {
                SpeechImpl.getInstance().startListen(null);
            }
        });
    }
}
