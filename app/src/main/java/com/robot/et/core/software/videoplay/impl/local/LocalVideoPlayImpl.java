package com.robot.et.core.software.videoplay.impl.local;

import android.content.Context;
import android.content.Intent;

import com.robot.et.core.software.videoplay.IVideoPlay;
import com.robot.et.core.software.videoplay.callback.VideoPlayCallBack;
import com.robot.et.core.software.videoplay.config.VideoPlayConfig;

/**
 * Created by houdeming on 2016/10/31.
 * 系统视频播放器
 */
public class LocalVideoPlayImpl implements IVideoPlay {
    private Context context;
    private static VideoPlayCallBack callBack;

    public LocalVideoPlayImpl(Context context) {
        this.context = context;
    }

    protected static VideoPlayCallBack getCallBack() {
        return callBack;
    }

    @Override
    public void play(String content, VideoPlayCallBack callBack) {
        this.callBack = callBack;
        Intent intent = new Intent(context, LocalVideoPlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    public void stopPlay() {
        Intent intent = new Intent();
        intent.setAction(VideoPlayConfig.ACTION_STOP_VIDEO);
        context.sendBroadcast(intent);
    }

    @Override
    public void closeVideo() {
        if (LocalVideoPlayActivity.instance != null) {
            LocalVideoPlayActivity.instance.finish();
            LocalVideoPlayActivity.instance = null;
        }
    }

    @Override
    public boolean isPlayVideo() {
        if (LocalVideoPlayActivity.instance != null) {
            return true;
        }
        return false;
    }
}
