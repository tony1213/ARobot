package com.robot.et.core.software.videocall.impl.agora;

import android.content.Context;
import android.content.Intent;

import com.robot.et.core.software.videocall.IVideoCall;

/**
 * Created by houdeming on 2016/10/29.
 * agora视频电话
 */
public class AgoraVideoImpl implements IVideoCall {

    private Context context;

    public AgoraVideoImpl(Context context) {
        this.context = context;
    }

    @Override
    public void callPhone(int callType, String roomNum, boolean isCallByVoice) {
        Intent intent = new Intent(context, AgoraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AgoraActivity.CALL_TYPE, callType);
        intent.putExtra(AgoraActivity.CALL_CHANNEL_ID, roomNum);
        intent.putExtra(AgoraActivity.CALL_IS_VOICE, isCallByVoice);
        context.startActivity(intent);
    }

    @Override
    public boolean isPhoneCallIng() {
        if (AgoraActivity.instance != null) {
            return true;
        }
        return false;
    }

    @Override
    public void closePhone() {
        if (AgoraActivity.instance != null) {
            AgoraActivity.instance.closeChannel();
            AgoraActivity.instance = null;
        }
    }
}
