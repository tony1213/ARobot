package com.robot.et.core.software.videocall;

import android.content.Context;

/**
 * Created by houdeming on 2016/11/2.
 * 视频电话的工厂接口
 */
public interface VideoCallFactory {
    /**
     * 创建视频电话
     *
     * @param context 上下文
     * @return
     */
    IVideoCall createVideoCall(Context context);
}
