package com.robot.et.core.software.voice.ifly;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * Created by houdeming on 2016/9/3.
 * 科大讯飞语音合成二次封装
 */
public class Speak extends Voice {
    private SpeechSynthesizer mTts;
    private ISpeak iSpeak;
    private Context context;

    public Speak(Context context, ISpeak iSpeak) {
        this.iSpeak = iSpeak;
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, initListener);
    }

    // 销毁当前类对象
    public void destroy() {
        if (mTts != null) {
            stopSpeak();
            mTts.destroy();
        }
    }

    /**
     * 科大讯飞语音合成 说话
     *
     * @param speakContent    要说的话
     * @param speakMen        发音人
     * @param speed           语速
     * @param pitch           语调
     * @param volume          音量
     * @param isNetwork      是否有网络
     * @return
     */
    public boolean speak(String speakContent, String speakMen, String speed, String pitch,
                         String volume, boolean isNetwork) {
        if (TextUtils.isEmpty(speakContent)) {
            return false;
        }
        if (mTts == null) {
            return false;
        }
        // 语音合成参数设置
        setTextToVoiceParam(speakMen, speed, pitch, volume, isNetwork);
        // 调用sdk提供的语音合成方法
        int code = mTts.startSpeaking(speakContent, mTtsListener);
        // * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
        // * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
        // String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
        // int code = mTts.synthesizeToUri(text, path, mTtsListener);

        // 语音合成失败
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
            } else {
                Log.i(TAG, "语音合成失败,错误码=== " + code);
            }
            return false;
        }
        return true;
    }

    // 停止说话
    public void stopSpeak() {
        if (mTts != null) {
            if (mTts.isSpeaking()) {
                mTts.stopSpeaking();
            }
        }
    }

    // 语音合成监听器
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        // 开始播放
        @Override
        public void onSpeakBegin() {
            Log.i(TAG, "  onSpeakBegin()");
            if (iSpeak != null) {
                iSpeak.onSpeakBegin();
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
            Log.i(TAG, "  onCompleted()");
            if (iSpeak != null) {
                iSpeak.onSpeakCompleted(error);
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

    /**
     * 科大讯飞语音合成参数设置
     *
     * @param speakMen 发音人
     * @param speed    语速
     * @param pitch    语调
     * @param volume   音量
     */
    private void setTextToVoiceParam(String speakMen, String speed, String pitch, String volume, boolean isNetwork) {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        if (isNetwork) {// 有网络
            // 根据合成引擎设置相应参数
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        } else {// 无网络
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath(speakMen));
        }
        // 设置合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, speakMen);
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
    private String getResourcePath(String speakMen){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + speakMen + ".jet"));
        return tempBuffer.toString();
    }
}
