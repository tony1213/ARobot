package com.robot.et.business.media;

import android.content.Context;
import android.util.Log;

import com.robot.et.app.CustomApplication;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.music.IMusic;
import com.robot.et.core.software.music.MusicFactory;
import com.robot.et.core.software.music.callback.MusicCallBack;

/**
 * Created by houdeming on 2016/11/16.
 * 音乐
 */

public class Music {

    private static final String TAG = "musicPlay";
    private static IMusic localPlay;
    private static IMusic xiMaLaYa;
    private static int musicType;
    public static final int MUSIC_FROM_NETWORK = 1;// 来自网络
    public static final int MUSIC_FROM_LOCAL = 2;// 来自本地


    static {
        Context context = CustomApplication.getInstance().getApplicationContext();
        localPlay = MusicFactory.produceLocalPlay(context);
        xiMaLaYa = MusicFactory.produceXiMaLaYaPlay(context);
    }

    /**
     * 播放音乐
     * @param context 上下文
     * @param playType 播放类型
     * @param content 播放内容
     * @param musicFrom 音乐来源
     */
    public static void playMusic(Context context, int playType, String content, int musicFrom) {
        GlobalConfig.isPlayMusic = true;
        musicType = musicFrom;
        Log.i(TAG, "content==" + content);
        playByLocal(context, playType, content);
    }

    /**
     * 使用本地系统播放器
     *
     * @param context  上下文
     * @param playType 播放类型
     * @param content  播放内容
     */
    private static void playByLocal(Context context, int playType, String content) {
//        if (localPlay == null) {
//            localPlay = MusicFactory.produceLocalPlay(context);
//        }
        localPlay.play(playType, content, musicCallBack);
    }

    /**
     * 使用喜马拉雅播放器
     *
     * @param context  上下文
     * @param playType 播放类型
     * @param content  播放内容
     */
    public static void playByXiMaLaYa(Context context, int playType, String content) {
//        if (xiMaLaYa == null) {
//            xiMaLaYa = MusicFactory.produceXiMaLaYaPlay(context);
//        }
        Log.i(TAG, "co==" + content);
        xiMaLaYa.play(playType, content, musicCallBack);
    }

    /**
     * 停止播放音乐
     */
    public static void stopMusic() {
        if (localPlay != null) {
            localPlay.stopPlay();
        }
        if (xiMaLaYa != null) {
            xiMaLaYa.stopPlay();
        }
    }

    private static MusicCallBack musicCallBack = new MusicCallBack() {
        @Override
        public void playStart() {
            Log.i(TAG, "playStart()");
        }

        @Override
        public void playComplected() {
            Log.i(TAG, "playComplected()");
            GlobalConfig.isPlayMusic = false;
            VoiceHandler.listen();
        }

        @Override
        public void playFail() {
            Log.i(TAG, "playFail()");
            GlobalConfig.isPlayMusic = false;
            VoiceHandler.speakEndToListen("音乐播放失败");
        }
    };
}
