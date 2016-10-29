package com.robot.et.business.video;

import android.content.Context;
import android.content.Intent;

/**
 * Created by houdeming on 2016/10/29.
 * 视频电话
 */
public class VideoPhone {
    /**呼叫电话
     *
     * @param context 上下文
     * @param callType 呼叫类型
     * @param roomNum 房间号
     * @param isCallByVoice 是否是语音呼叫
     */
    public static void callPhone(Context context, int callType, String roomNum, boolean isCallByVoice) {
        Intent intent = new Intent(context, AgoraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AgoraActivity.CALL_TYPE, callType);
        intent.putExtra(AgoraActivity.CALL_CHANNEL_ID, roomNum);
        intent.putExtra(AgoraActivity.CALL_IS_VOICE, isCallByVoice);
        context.startActivity(intent);
    }

    // 电话是否正在进行
    public static boolean isPhoneCallIng() {
        if (AgoraActivity.instance != null) {
            return true;
        }
        return false;
    }

    // 关闭电话
    public static void closePhone() {
        if (AgoraActivity.instance != null) {
            AgoraActivity.instance.closeChannel();
            AgoraActivity.instance = null;
        }
    }
}
