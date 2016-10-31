package com.robot.et.core.software.music.impl.local;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.core.software.music.callback.MusicCallBack;
import com.robot.et.core.software.music.config.MusicConfig;

import java.io.IOException;

/**
 * Created by houdeming on 2016/9/7.
 * 音乐播放工具
 */
public class LocalPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private MusicCallBack callBack;

    @Override
    public void onCreate() {
        super.onCreate();
        // 媒体播放器对象
        mediaPlayer = new MediaPlayer();
        //设置音乐播放完成时的监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                Log.i(MusicConfig.MUSIC_TAG, "音乐播放完成");
                if (callBack != null) {
                    callBack.playComplected();
                }
            }
        });

        callBack = LocalMusicPlayImpl.getCllBack();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicConfig.ACTION_STOP_MUSIC);
        registerReceiver(receiver, filter);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicConfig.ACTION_STOP_MUSIC)) {
                Log.i(MusicConfig.MUSIC_TAG, "停止音乐播放");
                stopPlay();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String content = intent.getStringExtra("content");
        play(content);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (mediaPlayer != null) {
            // 停止音乐播放
            stopPlay();
            // 释放对象
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 播放音乐
    private void play(String musicSrc) {
        boolean flag = false;
        if (!TextUtils.isEmpty(musicSrc)) {
            try {
                flag = true;
                mediaPlayer.reset();// 把各项参数恢复到初始状态
                mediaPlayer.setDataSource(musicSrc);
                // 进行缓冲
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new PreparedListener());
            } catch (IllegalStateException e) {
                Log.i(MusicConfig.MUSIC_TAG, "play IllegalStateException==" + e.getMessage());
                flag = false;
            } catch (IOException e) {
                Log.i(MusicConfig.MUSIC_TAG, "play IOException==" + e.getMessage());
                flag = false;
            }
        }
        if (!flag) {
            if (callBack != null) {
                callBack.playFail();
            }
        }
    }

    // 停止播放
    private void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
    }

    // 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.i(MusicConfig.MUSIC_TAG, "音乐开始播放");
            // 开始播放
            mediaPlayer.start();
            if (callBack != null) {
                callBack.playStart();
            }
        }
    }
}
