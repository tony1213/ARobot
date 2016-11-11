package com.robot.et.business.voice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.R;
import com.robot.et.app.CustomApplication;
import com.robot.et.business.control.MatchScene;
import com.robot.et.business.control.MoveOrder;
import com.robot.et.business.view.ViewManager;
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
    private static SpeakEndCallBack mSpeakEndCallBack;
    private static ListenResultCallBack mListenResultCallBack;
    private static final int VIEW_IMG = 1;

    static {
        context = CustomApplication.getInstance().getApplicationContext();
        iflyVoice = VoiceFactory.produceIflyVoice(context);
        turingVoice = VoiceFactory.produceTuringVoice(context);
    }

    /**
     * 主要是防止唤醒子线程中调用，不做任何处理
     */
    public static void init() {

    }

    /**
     * 说
     * @param speakContent 内容
     * @param callBack 说话完成的回调（可以为null）
     */
    public static void speak(String speakContent, SpeakEndCallBack callBack) {
        mSpeakEndCallBack = callBack;
        iflyVoice.startSpeak(speakContent, speakCallBack);
    }

    /**
     * 说完之后继续听
     * @param content 内容
     */
    public static void speakEndToListen(String content) {
        speak(content, speakEndCallBack);
    }

    /**
     * 听
     * @param callBack 听写结果回调
     */
    public static void listen(ListenResultCallBack callBack) {
        mListenResultCallBack = callBack;
        // 显示表情
        handler.sendEmptyMessage(VIEW_IMG);
        iflyVoice.startListen(listenCallBack);
    }

    /**
     * 听
     */
    public static void listen() {
        listen(listenResultCallBack);
    }

    private static SpeakCallBack speakCallBack = new SpeakCallBack() {
        @Override
        public void onSpeakBegin() {
            Log.i(TAG, "onSpeakBegin()");
        }

        @Override
        public void onSpeakEnd() {
            Log.i(TAG, "onSpeakEnd()");
            if (mSpeakEndCallBack != null) {
                mSpeakEndCallBack.onSpeakEnd();
            }
        }
    };

    private static ListenCallBack listenCallBack = new ListenCallBack() {
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
            // 音量值0-30
            Log.i(TAG, "volumeValue==" + volumeValue);
        }

        @Override
        public void onListenResult(String result) {
            Log.i(TAG, "onListenResult() result==" + result);
            if (!TextUtils.isEmpty(result)) {
                // 结果只有一个字的时候过滤掉，继续听
                if (result.length() == 1) {
                    listen();
                } else {
                    // 显示文字
                    ViewManager.getViewCallBack().onShowText(result);
                    if (mListenResultCallBack != null) {
                        mListenResultCallBack.onListenResult(result);
                    }
                }
            } else {
                listen();
            }
        }
    };

    private static ListenResultCallBack listenResultCallBack = new ListenResultCallBack() {
        @Override
        public void onListenResult(String result) {
            handleResult(result);
        }
    };

    private static SpeakEndCallBack speakEndCallBack = new SpeakEndCallBack() {
        @Override
        public void onSpeakEnd() {
            listen();
        }
    };

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
            if (MatchScene.isMatchScene(context, result)) {
                return;
            }
            if (MoveOrder.isControlMove(result)) {
                return;
            }
            understanderResult(result);
        }
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VIEW_IMG:// 显示表情
                    ViewManager.getViewCallBack().onShowEmotion(false, R.mipmap.emotion_normal);
                    break;
            }
        }
    };

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
                                if (understandResult.contains("&")) {
                                    String[] musics = understandResult.split("&");
                                    understandResult = musics[0] + musics[1];
                                }

                                break;
                            case SCHEDULE://提醒
                                // 日期 + 时间 + 说的日期 + 说的时间 + 做什么事（中间以&连接）
                                // 当只是通知的时候，中间没有&
                                if (understandResult.contains("&")) {
                                    String[] reminds = understandResult.split("&");
                                    understandResult = reminds[2] + reminds[3] + reminds[4];
                                }

                                break;
                            case WEATHER://天气查询
                                // 时间 + 城市 + 区域 + 天气（中间以&连接）
                                if (understandResult.contains("&")) {
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

                                }

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
                    speakEndToListen(understandResult);
                } else {
                    turingVoice.understanderText(content, new UnderstandCallBack() {
                        @Override
                        public void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult) {
                            Log.i(TAG, "tuling understandResult==" + understandResult);
                            if (!TextUtils.isEmpty(understandResult)) {
                                speakEndToListen(understandResult);
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
