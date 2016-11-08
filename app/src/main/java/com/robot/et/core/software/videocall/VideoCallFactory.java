package com.robot.et.core.software.videocall;

import android.content.Context;

import com.robot.et.core.software.videocall.impl.agora.AgoraVideoImpl;

/**
 * Created by houdeming on 2016/11/2.
 * 视频电话的工厂类
 */
public class VideoCallFactory {
    /**
     * 创建声网的视频电话
     *
     * @param context 上下文
     * @return
     */
    public static IVideoCall produceAgora(Context context) {
        return new AgoraVideoImpl(context);
    }
}
