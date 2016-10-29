package com.robot.et.core.software.voice.impl.ifly.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * Created by houdeming on 2016/10/29.
 * 语音参数设置
 */
public class IflyParameter {
    /**
     * 科大讯飞语音合成参数设置
     *
     * @param context 上下文
     * @param mTts 语音合成对象
     * @param speakMen 发音人
     * @param speed    语速
     * @param pitch    语调
     * @param volume   音量
     */
    public static void setTextToVoiceParam(Context context, SpeechSynthesizer mTts, String speakMen, String speed, String pitch, String volume) {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        if (TextUtils.equals(speakMen, SpeakConfig.SPEAK_MEN_LOCAL)) {// 本地
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath(context, speakMen));
        } else {// 云端
            // 根据合成引擎设置相应参数
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, speakMen);
        }
        // 设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, speed);
        // 设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, pitch);
        // 设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, volume);
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    //获取本地发音人资源路径
    private static String getResourcePath(Context context, String speakMen){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + speakMen + ".jet"));
        return tempBuffer.toString();
    }

    /**
     * 科大讯飞语音听写参数设置
     *
     * @param mIat 语音听写对象
     * @param language 听的语言
     */
    public static void setVoiceToTextParam(SpeechRecognizer mIat, String language) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        if (language.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, language);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }
}
