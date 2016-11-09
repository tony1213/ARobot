package com.robot.et.core.software.music.impl.local;

import android.content.Context;
import android.content.Intent;

import com.robot.et.core.software.music.IMusic;
import com.robot.et.core.software.music.callback.MusicCallBack;
import com.robot.et.core.software.music.config.MusicConfig;

/**
 * Created by houdeming on 2016/10/31.
 * 系统音乐播放器
 */
public class LocalMusicPlayImpl implements IMusic {
    private Context context;
    private Intent intent;
    private static MusicCallBack callBack;

    public LocalMusicPlayImpl(Context context) {
        this.context = context;
    }

    protected static MusicCallBack getCllBack() {
        return callBack;
    }

    @Override
    public void play(int playType, String content, MusicCallBack callBack) {
        this.callBack = callBack;
        intent = new Intent(context, LocalPlayerService.class);
        intent.putExtra("content", content);
        context.startService(intent);
    }

    @Override
    public void stopPlay() {
        Intent intent = new Intent();
        intent.setAction(MusicConfig.ACTION_STOP_MUSIC);
        context.sendBroadcast(intent);
    }

    @Override
    public void destroyPlayer() {
        if (intent != null) {
            context.stopService(intent);
        }
    }
}
