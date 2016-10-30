package com.robot.et.core.software.voice.impl.ifly;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.robot.et.core.software.voice.IVoice;
import com.robot.et.core.software.voice.callback.ListenCallBack;
import com.robot.et.core.software.voice.callback.ParseResultCallBack;
import com.robot.et.core.software.voice.callback.SceneServiceEnum;
import com.robot.et.core.software.voice.callback.SpeakCallBack;
import com.robot.et.core.software.voice.callback.UnderstandCallBack;
import com.robot.et.core.software.voice.impl.ifly.util.IflyParameter;
import com.robot.et.core.software.voice.impl.ifly.util.IflyResultParse;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by houdeming on 2016/10/29.
 * 讯飞语音
 */
public class XFVoice implements IVoice {
    private static final String TAG = "voice";
    private Context context;
    private SpeechSynthesizer mTts;
    private SpeakCallBack speakCallBack;
    private TextUnderstander mTextUnderstander;
    private UnderstandCallBack understandCallBack;
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private ListenCallBack listenCallBack;

    public XFVoice(Context context) {
        this.context = context;
        // 初始化合成对象
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(context, initListener);
        }
        // 初始化语音听写对象
        if (mIat == null) {
            mIat = SpeechRecognizer.createRecognizer(context, initListener);
        }
        // 初始化文本理解对象
        if (mTextUnderstander == null) {
            mTextUnderstander = TextUnderstander.createTextUnderstander(context, initListener);
        }
        // 上传词表
        uploadUserThesaurus("userwords", "userword");
    }

    // 初始化监听
    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {// 初始化失败,错误码
                Log.i(TAG, "InitListener  初始化失败,错误码==" + code);
            } else {// 初始化成功，之后可以调用startSpeaking方法
                Log.i(TAG, "InitListener  初始化成功");
            }
        }
    };

    private void cancelSpeak() {
        if (mTts != null) {
            if (mTts.isSpeaking()) {
                mTts.stopSpeaking();
            }
        }
    }

    private void cancelListen() {
        if (mIat != null) {
            if (mIat.isListening()) {
                mIat.cancel();
            }
        }
    }

    private void cancelUnderstand() {
        if (mTextUnderstander != null) {
            if (mTextUnderstander.isUnderstanding()) {
                Log.i(TAG, "文本理解取消");
                mTextUnderstander.cancel();
            }
        }
    }

    // 语音合成监听器
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        // 开始播放
        @Override
        public void onSpeakBegin() {
            Log.i(TAG, "onSpeakBegin()");
            if (speakCallBack != null) {
                speakCallBack.onSpeakBegin();
            }
        }
        // 暂停播放
        @Override
        public void onSpeakPaused() {

        }
        // 继续播放
        @Override
        public void onSpeakResumed() {

        }
        // 合成进度
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {

        }
        // 播放进度
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }
        // 合成完成
        @Override
        public void onCompleted(SpeechError error) {
            Log.i(TAG, "onCompleted()");
            if (speakCallBack != null) {
                speakCallBack.onSpeakEnd();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            // if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            // String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            // Log.d(TAG, "session id =" + sid);
            // }
        }
    };

    //听写监听器
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.i(TAG, "onBeginOfSpeech()");
            if (listenCallBack != null) {
                listenCallBack.onListenBegin();
            }
        }

        @Override
        public void onError(SpeechError error) {
            Log.i(TAG, "Speech onError");
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (listenCallBack != null) {
                listenCallBack.onListenResult("");
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.i(TAG, "结束说话");
            if (listenCallBack != null) {
                listenCallBack.onListenEnd();
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            // 获取语音听写返回的内容
            Log.i(TAG, "onResult");
            String result = IflyResultParse.printResult(results, mIatResults);
            if (isLast) {
                Log.i(TAG, "问题原版isLast result====" + result);
                if (listenCallBack != null) {
                    listenCallBack.onListenResult(result);
                }
            }
        }

        // 获取语音听写时说话音量的变化值
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.i(TAG, "当前正在说话，音量大小： volume==" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            Log.i(TAG, "onEvent");
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            // if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            // String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            // Log.d(TAG, "session id =" + sid);
            // }
        }
    };

    // 文本理解监听器
    private TextUnderstanderListener textListener = new TextUnderstanderListener() {

        @Override
        public void onResult(UnderstanderResult result) {// 理解成功
            Log.i(TAG, "文本理解onResult");
            Message message = handler.obtainMessage();
            message.obj = result;
            handler.sendMessage(message);
        }

        @Override
        public void onError(SpeechError error) {// 理解失败
            // 文本语义不能使用回调错误码14002，请确认您下载sdk时是否勾选语义场景和私有语义的发布
            int errorCode = error.getErrorCode();
            Log.i(TAG, "文本理解onError Code==" + errorCode);
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, "");
            }
        }
    };

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

    // 理解返回的内容处于子线程中，通过handler，把结果放到主线程中处理
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            UnderstanderResult result = (UnderstanderResult) msg.obj;
            Log.i(TAG, "文本理解onResult  result===" + result);
            String text = "";
            if (null != result) {
                text = result.getResultString();
                Log.i(TAG, "文本理解text===" + text);
                IflyResultParse.parseAnswerResult(text, new ParseResultCallBack() {
                    @Override
                    public void getResult(String question, String service, JSONObject jObject) {
                        SceneServiceEnum serviceEnum = getParseScene(service);
                        Log.i(TAG, "onSpeakCompleted serviceEnum==" + serviceEnum);
                        if (serviceEnum != null) {
                            String answer = "";
                            switch (serviceEnum) {
                                case BAIKE://百科
                                case CALC://计算器
                                case DATETIME://日期
                                case FAQ://社区问答
                                case OPENQA://褒贬&问候&情绪
                                case CHAT://闲聊
                                    answer = IflyResultParse.getAnswerData(jObject);

                                    break;

                                case COOKBOOK://菜谱
                                    answer = IflyResultParse.getCookBookData(jObject);

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
                                    answer = IflyResultParse.getMusicData(jObject);

                                    break;
                                case RESTAURANT://餐馆
                                    // do nothing

                                    break;
                                case SCHEDULE://提醒
                                    // 日期 + 时间 + 说的日期 + 说的时间 + 做什么事
                                    answer = IflyResultParse.getRemindData(jObject);

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
                                    answer = IflyResultParse.getWeatherData(jObject);

                                    break;
                                case TELEPHONE://打电话
                                    answer = IflyResultParse.getPhoneData(jObject);

                                    break;
                                case MESSAGE://发短信
                                    // do nothing

                                    break;
                                case PM25://空气质量
                                    answer = IflyResultParse.getPm25Data(jObject);

                                    break;
                                case RADIO://电台
                                    answer = IflyResultParse.getRadioName(jObject);

                                    break;
                            }

                            if (understandCallBack != null) {
                                understandCallBack.onUnderstandResult(serviceEnum, answer);
                            }

                        } else {
                            if (understandCallBack != null) {
                                understandCallBack.onUnderstandResult(null, "");
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMsg) {
                        if (understandCallBack != null) {
                            understandCallBack.onUnderstandResult(null, "");
                        }
                    }
                });
                return;
            }
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, text);
            }
        }
    };



    @Override
    public void startSpeak(String speakContent, String speakMen, SpeakCallBack callBack) {
        this.speakCallBack = callBack;
        if (mTts == null || TextUtils.isEmpty(speakContent)) {
            if (speakCallBack != null) {
                speakCallBack.onSpeakEnd();
            }
            return;
        }
        // 语音合成参数设置
        IflyParameter.setTextToVoiceParam(context, mTts, speakMen, "60", "50", "100");
        // 调用sdk提供的语音合成方法
        int code = mTts.startSpeaking(speakContent, mTtsListener);
        // * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
        // * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
        // String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
        // int code = mTts.synthesizeToUri(text, path, mTtsListener);
        // 语音合成失败
        if (code != ErrorCode.SUCCESS) {
            Log.i(TAG, "语音合成失败,错误码=== " + code);
            if (speakCallBack != null) {
                speakCallBack.onSpeakEnd();
            }
        }
    }

    @Override
    public void stopSpeak() {
        cancelSpeak();
    }

    @Override
    public void startListen(String listenMen, ListenCallBack callBack) {
        this.listenCallBack = callBack;
        if (mIat == null) {
            if (listenCallBack != null) {
                listenCallBack.onListenResult("");
            }
            return;
        }
        // 每次听之前要把上一次听的结果清除掉
        mIatResults.clear();
        // 语音听写参数设置
        IflyParameter.setVoiceToTextParam(mIat, listenMen);
        // 调用sdk提供的语音听写方法
        int ret = mIat.startListening(mRecognizerListener);
        // 语音听写返回的值
        if (ret != ErrorCode.SUCCESS) {
            Log.i(TAG, "listen  听写失败 ret===" + ret);
            if (listenCallBack != null) {
                listenCallBack.onListenResult("");
            }
        }
    }

    @Override
    public void stopListen() {
        cancelListen();
    }

    @Override
    public void understanderText(String content, UnderstandCallBack callBack) {
        this.understandCallBack = callBack;
        if (mTextUnderstander == null || TextUtils.isEmpty(content)) {
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, "");
            }
            return;
        }
        // 每次理解之前一定要把上一次的理解取消，避免造成影响
        cancelUnderstand();
        // 调用sdk提供的理解方法
        int ret = mTextUnderstander.understandText(content, textListener);
        // 理解失败
        if (ret != ErrorCode.SUCCESS) {
            Log.i(TAG, "文本理解错误码ret==" + ret);
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, "");
            }
        }
    }

    @Override
    public void destroyVoice() {
        if (mTts != null) {
            cancelSpeak();
            mTts.destroy();
        }
        if (mIat != null) {
            cancelListen();
            mIat.destroy();
        }
        if (mTextUnderstander != null) {
            cancelUnderstand();
            mTextUnderstander.destroy();
        }
    }

    //上传词表监听器
    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                Log.i(TAG, "上传联系人词表error===" + error.toString());
                uploadUserThesaurus("userwords", "userword");
            } else {
                Log.i(TAG, "上传联系人词表成功");
            }
        }
    };

    // 上传词表
    private boolean uploadUserThesaurus(String thesaurusName, String thesaurusSign) {
        String contents = readFile(context, thesaurusName, "utf-8");
        // 指定引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 置编码类型
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        int code = mIat.updateLexicon(thesaurusSign, contents, mLexiconListener);
        if (code != ErrorCode.SUCCESS) {
            uploadUserThesaurus("userwords", "userword");
            return false;
        }
        return true;
    }

    //读取asset目录下文件
    private String readFile(Context context, String file, String code) {
        AssetManager am = context.getAssets();
        String result = "";
        try {
            InputStream in = am.open(file);
            int len = in.available();
            byte[] buf = new byte[len];
            in.read(buf, 0, len);
            result = new String(buf, code);
            in.close();
        } catch (Exception e) {
            Log.i(TAG, "readFile Exception==" + e.getMessage());
        }
        return result;
    }
}
