package com.robot.et.business.voice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechError;
import com.robot.et.business.common.VoiceResultHandler;
import com.robot.et.config.DataConfig;
import com.robot.et.core.software.voice.ifly.ISpeak;
import com.robot.et.core.software.voice.ifly.ITextUnderstand;
import com.robot.et.core.software.voice.ifly.IVoiceDictate;
import com.robot.et.core.software.voice.ifly.Speak;
import com.robot.et.core.software.voice.ifly.TextUnderstand;
import com.robot.et.core.software.voice.ifly.VoiceDictate;
import com.robot.et.core.software.voice.turing.ITuring;
import com.robot.et.core.software.voice.turing.Turing;

import org.json.JSONObject;

/**
 * Created by houdeming on 2016/10/26.
 * 语音业务
 */
public class VoiceService extends Service implements ISpeak, IVoiceDictate, ITextUnderstand, ITuring {
    private final String TAG = "listen";
    private Speak mSpeak;
    private TextUnderstand mTextUnderstand;
    private VoiceDictate mVoiceDictate;
    private Turing mTuring;
    private SpeakCallBack speakCallBack;
    private ListenCallBack listenCallBack;
    private UnderstandCallBack understandCallBack;
    private String mContent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "VoiceService onCreate()");
        // 初始化语音合成
        mSpeak = new Speak(this, this);
        // 初始化科大讯飞文本理解
        mTextUnderstand = new TextUnderstand(this, this);
        // 初始化语音听写
        mVoiceDictate = new VoiceDictate(this, this);
        // 初始化图灵文本理解
        mTuring = new Turing(this, this, DataConfig.TURING_SECRET, DataConfig.TURING_APPID, DataConfig.TURING_UNIQUEID);
        // 初始化对外调用接口
        SpeechImpl.getInstance().setService(this);
        // 上传词表
        uploadThesaurus();
    }

    // 上传词表
    private void uploadThesaurus() {
        boolean isSuccess = mVoiceDictate.uploadUserThesaurus("userwords", "userword");
        if (isSuccess) {
            Log.i(TAG, "上传词表成功");
        } else {
            Log.i(TAG, "上传词表失败");
            uploadThesaurus();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁说
        mSpeak.destroy();
        // 销毁听
        mVoiceDictate.destroy();

    }

    // 开始说话
    public void startSpeak(String speakContent, SpeakCallBack callBack) {
        speakCallBack = callBack;
        // 说话
        boolean isSpeakSuccess = mSpeak.speak(speakContent, DataConfig.SPEAK_MEN_CLOUD, "60", "50", "100", true);
        // 语音合成失败
        if (!isSpeakSuccess) {
            if (speakCallBack != null) {
                speakCallBack.onSpeakEnd(false);
            }
        }
    }

    // 取消说话
    public void cancelSpeak() {
        mSpeak.stopSpeak();
    }

    // 开始听
    public void startListen(ListenCallBack callBack) {
        listenCallBack = callBack;
        beginListen();
    }

    // 开始听
    private void beginListen() {
        boolean isSuccess = mVoiceDictate.listen(DataConfig.SPEAK_MEN_CLOUD);
        if (!isSuccess) {
            beginListen();
        }
    }

    // 取消听
    public void cancelListen() {
        mVoiceDictate.stopListen();
    }

    // 要理解的内容
    public void understanderText(String content, UnderstandCallBack callBack) {
        Log.i(TAG, "understanderText  content==" + content);
        understandCallBack = callBack;
        // 优先使用科大讯飞文本理解，不能理解的话，再使用图灵文本理解
        if (!TextUtils.isEmpty(content)) {
            mContent = content;
            // 科大讯飞理解
            boolean isSuccess = mTextUnderstand.understandText(content);
            // 如果科大讯飞不能理解的话让图灵理解
            if (!isSuccess) {
                understandByTuring(content);
            }
        } else {
            if (understandCallBack != null) {
                understandCallBack.onResult(null, "");
            }
        }
    }

    // 图灵理解
    private void understandByTuring(String content) {
        boolean isTuringSuccess = mTuring.understandText(content);
        if (!isTuringSuccess) {
            if (understandCallBack != null) {
                understandCallBack.onResult(null, "");
            }
        }
    }

    // 获取解析的场景
    private SceneServiceEnum getParseScene(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (SceneServiceEnum serviceEnum : SceneServiceEnum.values()) {
                if (TextUtils.equals(str, serviceEnum.getServiceKey())) {
                    return serviceEnum;
                }
            }
        }
        return null;
    }

    // 实现ISpeak接口  开始说
    @Override
    public void onSpeakBegin() {
        Log.i(TAG, "onSpeakBegin");
    }

    // 实现ISpeak接口  说完成
    @Override
    public void onSpeakCompleted(SpeechError error) {
        Log.i(TAG, "onSpeakCompleted");
        if (error == null) {
            if (speakCallBack != null) {
                speakCallBack.onSpeakEnd(true);
            }
        } else {
            Log.i(TAG, "onSpeakCompleted error==" + error.getErrorCode());
            if (speakCallBack != null) {
                speakCallBack.onSpeakEnd(false);
            }
        }
    }

    // 实现ITextUnderstand接口  理解的结果
    @Override
    public void onUnderstandResult(String result) {
        Log.i(TAG, "onUnderstandResult result==" + result);
        if (TextUtils.isEmpty(result)) {
            understandByTuring(mContent);
            return;
        }

        UnderstandResultParse.parseAnswerResult(result, new ParseResultCallBack() {
            @Override
            public void getResult(String question, String service, JSONObject jObject) {
                SceneServiceEnum serviceEnum = getParseScene(service);
                Log.i(TAG, "onSpeakCompleted serviceEnum==" + serviceEnum);
                if (serviceEnum != null) {
                    String answer = "";
                    switch (serviceEnum) {
                        case BAIKE://百科
                            answer = UnderstandResultParse.getAnswerData(jObject);

                            break;
                        case CALC://计算器
                            answer = UnderstandResultParse.getAnswerData(jObject);

                            break;
                        case COOKBOOK://菜谱
                            answer = UnderstandResultParse.getCookBookData(jObject);

                            break;
                        case DATETIME://日期
                            answer = UnderstandResultParse.getAnswerData(jObject);

                            break;
                        case FAQ://社区问答
                            answer = UnderstandResultParse.getAnswerData(jObject);

                            break;
                        case FLIGHT://航班查询
                            // do nothing

                            break;
                        case HOTEL://酒店查询
                            // do nothing

                            break;
                        case MAP://地图查询
                            // do nothing

                            break;
                        case MUSIC://音乐
                            answer = UnderstandResultParse.getMusicData(jObject);

                            break;
                        case RESTAURANT://餐馆
                            // do nothing

                            break;
                        case SCHEDULE://提醒
                            // 日期 + 时间 + 说的日期 + 说的时间 + 做什么事
                            answer = UnderstandResultParse.getRemindData(jObject);

                            break;
                        case STOCK://股票查询
                            // do nothing

                            break;
                        case TRAIN://火车查询
                            // do nothing

                            break;
                        case TRANSLATION://翻译
                            // do nothing

                            break;
                        case WEATHER://天气查询
                            String city = "上海市";
                            String area = "浦东新区";
                            answer = UnderstandResultParse.getWeatherData(jObject, city, area);

                            break;
                        case OPENQA://褒贬&问候&情绪
                            answer = UnderstandResultParse.getAnswerData(jObject);

                            break;
                        case TELEPHONE://打电话
                            answer = UnderstandResultParse.getPhoneData(jObject);

                            break;
                        case MESSAGE://发短信
                            // do nothing

                            break;
                        case CHAT://闲聊
                            answer = UnderstandResultParse.getAnswerData(jObject);

                            break;
                        case PM25://空气质量
                            String mCity = "上海市";
                            String mArea = "浦东新区";
                            answer = UnderstandResultParse.getPm25Data(jObject, mCity, mArea);

                            break;
                        case RADIO://电台
                            answer = UnderstandResultParse.getRadioName(jObject);

                            break;
                    }

                    if (TextUtils.isEmpty(answer)) {
                        understandByTuring(mContent);
                    } else {
                        if (understandCallBack != null) {
                            understandCallBack.onResult(serviceEnum, answer);
                        }
                    }

                } else {
                    understandByTuring(mContent);
                }
            }

            @Override
            public void onError(String errorMsg) {
                understandByTuring(mContent);
            }
        });
    }

    // 实现ITextUnderstand接口   理解错误
    @Override
    public void onUnderstandError(int errorCode) {
        Log.i(TAG, "onUnderstandError");
        understandByTuring(mContent);
    }

    // 实现ITuring接口   图灵理解结果
    @Override
    public void onTuringResult(String result) {
        Log.i(TAG, "onTuringResult result===" + result);
        if (!TextUtils.isEmpty(result)) {
            // 对天气的结果特殊处理
            if (result.contains(":") && result.contains("周") && result.contains("风") && result.contains(";")) {
                Log.i(TAG, "从科大讯飞没有获取到天气问图灵");
                result = getWeatherContent(result);
            }
        }
        if (understandCallBack != null) {
            understandCallBack.onResult(null, result);
        }
    }

    // 实现ITuring接口   图灵理解错误
    @Override
    public void onTuringError(String errorMsg) {
        Log.i(TAG, "onTuringError errorMsg==" + errorMsg);
        if (understandCallBack != null) {
            understandCallBack.onResult(null, "");
        }
    }

    // 实现IVoiceDictate接口   开始听
    @Override
    public void onListenBegin() {
        Log.i(TAG, "onListenBegin");
    }

    // 实现IVoiceDictate接口   听结束
    @Override
    public void onListenEnd() {
        Log.i(TAG, "onListenEnd");
    }

    // 实现IVoiceDictate接口   听的时候语音变化值
    @Override
    public void onListenVolumeChanged(int volume, byte[] data) {
        Log.i(TAG, "onListenVolumeChanged volume==" + volume);
    }

    // 实现IVoiceDictate接口  听异常
    @Override
    public void onListenError(SpeechError error) {
        Log.i(TAG, "onListenError");
        beginListen();
    }

    // 实现IVoiceDictate接口   听的结果
    @Override
    public void onListenResult(String result) {
        Log.i(TAG, "onListenResult result==" + result);
        if (!TextUtils.isEmpty(result)) {
            // 结果只有一个字的时候过滤掉，继续听
            if (result.length() == 1) {
                beginListen();
                return;
            }
            if (listenCallBack != null) {
                listenCallBack.onListenResult(result);
            } else {
                VoiceResultHandler.handVoiceResult(VoiceService.this, result);
            }
            return;
        }
        beginListen();
    }

    /*
       * 对图灵返回的天气进行处理
       * content weather格式 上海:05/16 周一,15-24° 23° 晴 北风微风; 05/17 周二,16-26° 晴 东南风微风;
       * 05/18 周三,17-26° 多云 东风微风; 05/19 周四,19-26° 多云 东风微风;
       */
    private String getWeatherContent(String content) {
        String result = "";
        if (!TextUtils.isEmpty(content)) {
            String[] datas = content.split(";");
            result = datas[0];
            String[] tempDatas = result.split(",");
            result = tempDatas[1];
            String cityData = tempDatas[0];
            String[] citys = cityData.split("\\:");
            String city = citys[0];
            String[] weathers = result.split(" ");
            // weathers[0]15-24° weathers[1]23° weathers[2]晴 weathers[3]北风微风
            if (weathers != null && weathers.length > 0) {
                StringBuffer buffer = new StringBuffer(1024);
                buffer.append(city).append("市").append("天气：").append(weathers[2]).append(",气温：").append(weathers[0]).append(",风力：").append(weathers[3]);
                result = buffer.toString();
            }
        }
        return result;
    }
}
