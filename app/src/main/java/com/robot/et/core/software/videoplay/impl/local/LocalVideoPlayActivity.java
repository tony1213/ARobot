package com.robot.et.core.software.videoplay.impl.local;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.robot.et.R;
import com.robot.et.core.software.videoplay.callback.VideoPlayCallBack;
import com.robot.et.core.software.videoplay.config.VideoPlayConfig;

import java.io.File;

public class LocalVideoPlayActivity extends Activity {
    private SurfaceView sv;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int currentPosition = 0;
    private static boolean isPlaying;
    public static LocalVideoPlayActivity instance;
    private String fileSrc;
    private VideoPlayCallBack mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        instance = this;
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        sv = (SurfaceView) findViewById(R.id.sv);

        Intent intent = getIntent();
        fileSrc = intent.getStringExtra("content");

        mCallBack = LocalVideoPlayImpl.getCallBack();
        // 为SurfaceHolder添加回调
        sv.getHolder().addCallback(callback);

        // 4.0版本之下需要设置的属性
        // 设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到界面
        // sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 为进度条添加进度更改事件
        seekBar.setOnSeekBarChangeListener(change);

        IntentFilter filter = new IntentFilter();
        filter.addAction(VideoPlayConfig.ACTION_STOP_VIDEO);
        registerReceiver(receiver, filter);
    }

    // SurfaceHolder被修改的时候回调
    private Callback callback = new Callback() {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "SurfaceHolder 被销毁");
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "SurfaceHolder 被创建");
            if (currentPosition > 0) {
                // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
                play(currentPosition, fileSrc);
                currentPosition = 0;
            } else {
                play(0, fileSrc);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "SurfaceHolder 大小被改变");
        }

    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(VideoPlayConfig.ACTION_STOP_VIDEO)) {
                Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "停止播放视频");
                pausePlay();
            }
        }
    };

    private OnSeekBarChangeListener change = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 当进度条停止修改的时候触发
            // 取得当前进度条的刻度
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "onStopTrackingTouch");
            int progress = seekBar.getProgress();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                // 设置当前播放的位置
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "onStartTrackingTouch");
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        instance = null;
        unregisterReceiver(receiver);
        if (mediaPlayer != null) {
            // 停止音乐播放
            pausePlay();
            // 释放对象
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 停止播放视频
    private void pausePlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
    }

    /**
     * 开始播放
     *
     * @param msec 播放初始位置
     */
    private void play(final int msec, final String fileSrc) {
        if (TextUtils.isEmpty(fileSrc)) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "路径为空");
            if (mCallBack != null) {
                mCallBack.playFail();
            }
            return;
        }
        Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "path==" + fileSrc);

        File file = new File(fileSrc);
        if (!file.exists()) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "文件不存在");
            if (mCallBack != null) {
                mCallBack.playFail();
            }
            return;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mediaPlayer.setDataSource(file.getAbsolutePath());
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(sv.getHolder());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "开始播放");
                    mediaPlayer.start();
                    // 按照初始位置播放
                    mediaPlayer.seekTo(msec);
                    // 设置进度条的最大进度为视频流的最大播放时长
                    seekBar.setMax(mediaPlayer.getDuration());
                    // 开始线程，更新进度条的刻度
                    new Thread() {

                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer
                                            .getCurrentPosition();
                                    seekBar.setProgress(current);

                                    sleep(500);
                                }
                            } catch (Exception e) {
                                Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "Thread Exception");
                            }
                        }
                    }.start();

                    if (mCallBack != null) {
                        mCallBack.playStart();
                    }

                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "播放完毕");
                    isPlaying = false;
                    mediaPlayer.release();
                    if (mCallBack != null) {
                        mCallBack.playComplected();
                    }
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, " 发生错误重新播放");
                    isPlaying = false;
                    if (mCallBack != null) {
                        mCallBack.playFail();
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Log.i(VideoPlayConfig.VIDEO_PLAY_TAG, "发生错误重新播放 Exception");
            if (mCallBack != null) {
                mCallBack.playFail();
            }
        }

    }
}
