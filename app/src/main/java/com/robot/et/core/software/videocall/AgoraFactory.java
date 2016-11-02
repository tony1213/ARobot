package com.robot.et.core.software.videocall;

import android.content.Context;

import com.robot.et.core.software.videocall.impl.agora.AgoraVideoImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class AgoraFactory implements VideoCallFactory {
    @Override
    public IVideoCall createVideoCall(Context context) {
        return new AgoraVideoImpl(context);
    }
}
