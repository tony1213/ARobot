package com.robot.et.business.voice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.core.software.voice.IVoice;
import com.robot.et.core.software.voice.IflyVoiceFactory;
import com.robot.et.core.software.voice.TuringVoiceFactory;
import com.robot.et.core.software.voice.callback.ListenCallBack;
import com.robot.et.core.software.voice.callback.SpeakCallBack;
import com.robot.et.core.software.voice.callback.UnderstandCallBack;
import com.robot.et.core.software.voice.impl.ifly.util.SpeakConfig;
import com.robot.et.core.software.voice.voiceenum.SceneServiceEnum;

/**
 * Created by houdeming on 2016/10/26.
 * 语音业务
 */
public class VoiceService extends Service {
    private final String TAG = "VoiceService";
    private IVoice xfVoice;
    private IVoice turingVoice;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        xfVoice = new IflyVoiceFactory().createVoice(this);
        turingVoice = new TuringVoiceFactory().createVoice(this);

        listen();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void listen() {
        xfVoice.startListen(SpeakConfig.SPEAK_CLOUD_NANNAN, new ListenCallBack() {
            @Override
            public void onListenBegin() {
                Log.i(TAG, "onListenBegin()");
            }

            @Override
            public void onListenEnd() {
                Log.i(TAG, "onListenEnd()");
            }

            @Override
            public void onListenResult(String result) {
                Log.i(TAG, "result==" + result);
                if (!TextUtils.isEmpty(result)) {
                    understand(result);
                } else {
                    listen();
                }
            }
        });
    }

    private void understand(final String result) {
        xfVoice.understanderText(result, new UnderstandCallBack() {
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
                                    understand(weathers[0] + city + "的天气");
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
                                    understand(mCity + mArea + "的pm25");
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
                    speak(understandResult);
                } else {
                    turingVoice.understanderText(result, new UnderstandCallBack() {
                        @Override
                        public void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult) {
                            Log.i(TAG, "tuling understandResult==" + understandResult);
                            if (!TextUtils.isEmpty(understandResult)) {
                                speak(understandResult);
                            } else {
                                listen();
                            }
                        }
                    });
                }
            }
        });
    }

    private void speak(String content) {
        xfVoice.startSpeak(content, SpeakConfig.SPEAK_CLOUD_NANNAN, new SpeakCallBack() {
            @Override
            public void onSpeakBegin() {
                Log.i(TAG, "onSpeakBegin()");
            }

            @Override
            public void onSpeakEnd() {
                Log.i(TAG, "onSpeakEnd()");
                listen();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xfVoice.destroyVoice();
    }
}
