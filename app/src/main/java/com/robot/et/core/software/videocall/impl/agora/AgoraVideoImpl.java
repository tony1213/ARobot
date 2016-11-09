package com.robot.et.core.software.videocall.impl.agora;

import android.content.Context;
import android.content.Intent;

import com.robot.et.core.software.videocall.IVideoCall;
import com.robot.et.core.software.videocall.callback.PhoneCallBack;

/**
 * Created by houdeming on 2016/10/29.
 * agora视频电话
 */
public class AgoraVideoImpl implements IVideoCall {

    private Context context;
    private static PhoneCallBack callBack;

    public AgoraVideoImpl(Context context) {
        this.context = context;
    }

    protected static PhoneCallBack getCallBack() {
        return callBack;
    }

    @Override
    public void callPhone(int callType, String roomNum, PhoneCallBack callBack) {
        this.callBack = callBack;
        Intent intent = new Intent(context, AgoraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AgoraActivity.CALL_TYPE, callType);
        intent.putExtra(AgoraActivity.CALL_CHANNEL_ID, roomNum);
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
            AgoraActivity.instance.finish();
            AgoraActivity.instance = null;
        }
    }
}
