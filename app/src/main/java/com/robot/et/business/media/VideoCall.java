package com.robot.et.business.media;

import android.content.Context;
import android.util.Log;

import com.robot.et.core.software.videocall.IVideoCall;
import com.robot.et.core.software.videocall.VideoCallFactory;
import com.robot.et.core.software.videocall.callback.PhoneCallBack;

/**
 * Created by houdeming on 2016/11/17.
 * 拨打视频电话
 */

public class VideoCall {

    private static final String TAG = "videoPhone";
    private static IVideoCall videoCall;
    public static final int CALL_BY_OWN = 1;
    public static final int CALL_BY_APP = 2;
    private static int mCallFrom;


    /**
     * 呼叫视频电话
     * @param context 上下文
     * @param callType 呼叫类型 (视频，语音，查看)
     * @param roomNum 房间号
     * @param callFrom 视频的出处 (自己拨打还是App拨打过来的)
     */
    public static void call(Context context, int callType, String roomNum, int callFrom) {
        if (videoCall == null) {
            videoCall = VideoCallFactory.produceAgora(context);
        }
        mCallFrom = callFrom;
        videoCall.callPhone(callType, roomNum, phoneCallBack);
    }

    /**
     * 关闭视频电话
     */
    public static void closeVideoCall() {
        if (videoCall != null) {
            videoCall.closePhone();
        }
    }

    private static PhoneCallBack phoneCallBack = new PhoneCallBack() {
        @Override
        public void onPhoneConnectIng() {
            Log.i(TAG, "onPhoneConnectIng()");
        }

        @Override
        public void onPhoneConnect() {
            Log.i(TAG, "onPhoneConnect()");
        }

        @Override
        public void onPhoneDisconnect() {
            Log.i(TAG, "onPhoneDisconnect()");
        }

        @Override
        public void onPhoneError(String errorMsg) {
            Log.i(TAG, "onPhoneError() errorMsg==" + errorMsg);
        }
    };
}
