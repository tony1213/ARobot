package com.robot.et.business.control;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.control.util.NetResultParse;
import com.robot.et.business.media.VideoCall;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.business.voice.callback.SpeakEndCallBack;
import com.robot.et.config.RequestConfig;
import com.robot.et.core.software.push.IPush;
import com.robot.et.core.software.push.PushFactory;
import com.robot.et.core.software.videocall.config.VideoCallConfig;
import com.robot.et.entity.JpushInfo;

/**
 * Created by houdeming on 2016/11/17.
 */

public class Push implements IPush {

    private static final String TAG = "pushHand";
    private static Context mContext;

    public Push(Context context, String content) {
        mContext = context;
        PushFactory.setPushParameter(content);
        PushFactory.setPushCallBack(this);
    }

    @Override
    public void onPushResult(String result) {
        Log.i(TAG, "result==" + result);
        if (!TextUtils.isEmpty(result)) {
            JpushInfo info = new JpushInfo();
            if (result.contains("msg")) {
                info = NetResultParse.getJpushInfo(result);
            }
            if (info != null) {
                String direction = info.getDirection();
                Log.i(TAG, "direction===" + direction);
                if (TextUtils.isEmpty(direction)) {
                    doPushResult(info);
                    return;
                }
            }
        }
    }

    // 处理推送的结果
    private static void doPushResult(JpushInfo info) {
        int extra = info.getExtra();
        String musicContent = info.getMusicContent();
        roomNum = info.getRoomNum();
        content = info.getContent();
        Log.i(TAG, "pushCode===" + extra + "--musicContent===" + musicContent);
        Log.i(TAG, "roomNum===" + roomNum + "--content==" + content);
        switch (extra) {
            case RequestConfig.JPUSH_CALL_VIDEO:// 视频
                Log.i(TAG, "视频");
                callBefore();
                type = 1;
                VoiceHandler.speak(content, callBack);

                break;
            case RequestConfig.JPUSH_CALL_VOICE:// 语音
                Log.i(TAG, "语音");
                callBefore();
                type = 2;
                VoiceHandler.speak(content, callBack);

                break;
            case RequestConfig.JPUSH_CALL_LOOK:// 查看
                Log.i(TAG, "查看");
                callBefore();
                VideoCall.call(mContext, VideoCallConfig.CALL_TYPE_LOOK, roomNum, VideoCall.CALL_BY_APP);

                break;
            case RequestConfig.JPUSH_ROBOT_SPEAK:// 机器人学习库，通过说话学习
                Log.i(TAG, "机器人问答库通过说话学习");
                Navigation.goDesignedLocation(musicContent);

                break;
        }
    }

    private static int type = 0;
    private static String content;
    private static String roomNum;

    private static SpeakEndCallBack callBack = new SpeakEndCallBack() {
        @Override
        public void onSpeakEnd() {
            if (type == 1) {
                VideoCall.call(mContext, VideoCallConfig.CALL_TYPE_VIDEO, roomNum, VideoCall.CALL_BY_APP);
            } else {
                VideoCall.call(mContext, VideoCallConfig.CALL_TYPE_VOICE, roomNum, VideoCall.CALL_BY_APP);
            }
        }
    };

    private static void callBefore() {
        VoiceHandler.stopSpeak();
        VoiceHandler.stopListen();
    }
}
