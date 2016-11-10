package com.robot.et.business.voice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.app.CustomApplication;
import com.robot.et.business.voice.callback.ListenResultCallBack;
import com.robot.et.business.voice.callback.SpeakEndCallBack;
import com.robot.et.core.software.voice.IVoice;
import com.robot.et.core.software.voice.VoiceFactory;
import com.robot.et.core.software.voice.callback.ListenCallBack;
import com.robot.et.core.software.voice.callback.SpeakCallBack;
import com.robot.et.core.software.voice.callback.UnderstandCallBack;
import com.robot.et.core.software.voice.voiceenum.SceneServiceEnum;

/**
 * Created by houdeming on 2016/11/9.
 * 业务层对语音的统一封装，业务层调用。
 */
public class VoiceHandler {

    private static final String TAG = "voiceBusiness";
    private static Context context;
    private static IVoice iflyVoice;
    private static IVoice turingVoice;

    static {
        context = CustomApplication.getInstance().getApplicationContext();
        iflyVoice = VoiceFactory.produceIflyVoice(context);
        turingVoice = VoiceFactory.produceTuringVoice(context);
    }

    /**
     * 说
     * @param speakContent 内容
     * @param callBack 说话完成的回调（可以为null）
     */
    public static void speak(String speakContent, final SpeakEndCallBack callBack) {
        iflyVoice.startSpeak(speakContent, new SpeakCallBack() {
            @Override
            public void onSpeakBegin() {
                Log.i(TAG, "onSpeakBegin()");
            }

            @Override
            public void onSpeakEnd() {
                Log.i(TAG, "onSpeakEnd()");
                if (callBack != null) {
                    callBack.onSpeakEnd();
                }
            }
        });
    }

    /**
     * 说完之后继续听
     * @param content 内容
     */
    public static void speakEndToListen(String content) {
        speak(content, new SpeakEndCallBack() {
            @Override
            public void onSpeakEnd() {
                listen(new ListenResultCallBack() {
                    @Override
                    public void onListenResult(String result) {
                        handleResult(result);
                    }
                });
            }
        });
    }

    /**
     * 听
     * @param callBack 听写结果回调
     */
    public static void listen(final ListenResultCallBack callBack) {
        iflyVoice.startListen(new ListenCallBack() {
            @Override
            public void onListenBegin() {
                Log.i(TAG, "onListenBegin()");
            }

            @Override
            public void onListenEnd() {
                Log.i(TAG, "onListenEnd()");
            }

            @Override
            public void onVolumeChanged(int volumeValue) {
                Log.i(TAG, "volumeValue==" + volumeValue);
            }

            @Override
            public void onListenResult(String result) {
                Log.i(TAG, "onListenResult() result==" + result);
                if (!TextUtils.isEmpty(result)) {
                    // 结果只有一个字的时候过滤掉，继续听
                    if (result.length() == 1) {
                        listen(callBack);
                    } else {
                        if (callBack != null) {
                            callBack.onListenResult(result);
                        }
                    }
                } else {
                    listen(callBack);
                }
            }
        });
    }

    /**
     * 停止说
     */
    public static void stopSpeak() {
        iflyVoice.stopSpeak();
    }

    /**
     * 停止听
     */
    public static void stopListen() {
        iflyVoice.stopListen();
    }

    /**
     * 处理语音结果
     * @param result 语音内容
     */
    public static void handleResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            understanderResult(result);
        }
    }

    /**
     * 文本理解结果
     * @param content 要理解的内容
     */
    private static void understanderResult(final String content) {
        iflyVoice.understanderText(content, new UnderstandCallBack() {
            @Override
            public void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult) {
                Log.i(TAG, "serviceEnum==" + serviceEnum);
                Log.i(TAG, "ifly understandResult==" + understandResult);
                if (!TextUtils.isEmpty(understandResult)) {
                    if (serviceEnum != null) {
                        switch (serviceEnum) {
                            case MUSIC://音乐
                                // 歌手 + 歌名 + 歌曲src（中间以&连接）
                                String[] musics = understandResult.split("&");
                                understandResult = musics[0] + musics[1];

                                break;
                            case SCHEDULE://提醒
                                // 日期 + 时间 + 说的日期 + 说的时间 + 做什么事（中间以&连接）
                                String[] reminds = understandResult.split("&");
                                understandResult = reminds[2] + reminds[3] + reminds[4];

                                break;
                            case WEATHER://天气查询
                                // 时间 + 城市 + 区域 + 天气（中间以&连接）
                                String[] weathers = understandResult.split("&");
                                if (TextUtils.isEmpty(weathers[1])) {
                                    String city = "上海";
                                    understanderResult(weathers[0] + city + "的天气");
                                    return;
                                }
                                if (TextUtils.isEmpty(weathers[2])) {
                                    String area = "浦东新区";
                                    weathers[2] = area;
                                }

                                understandResult = weathers[0] + weathers[1] + weathers[2] + weathers[3];

                                break;
                            case TELEPHONE://打电话
                                // 人名或者电话号码

                                break;
                            case PM25://空气质量
                                // 城市 + 区域 + 空气质量（中间以&连接）
                                String mArea = "浦东新区";
                                String[] pms = understandResult.split("&");
                                if (TextUtils.isEmpty(pms[0])) {
                                    String mCity = "上海";
                                    understanderResult(mCity + mArea + "的pm25");
                                    return;
                                }
                                if (TextUtils.isEmpty(pms[1])) {
                                    pms[1] = mArea;
                                }

                                understandResult = pms[0] + pms[1] + pms[2];

                                break;
                            case RADIO://电台
                                // 电台名字

                                break;
                        }

                    }
                    speak(understandResult, null);
                } else {
                    turingVoice.understanderText(content, new UnderstandCallBack() {
                        @Override
                        public void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult) {
                            Log.i(TAG, "tuling understandResult==" + understandResult);
                            if (!TextUtils.isEmpty(understandResult)) {
                                speak(understandResult, null);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 销毁语音
     */
    public static void destroyVoice() {
        iflyVoice.destroyVoice();
        turingVoice.destroyVoice();
    }
}
